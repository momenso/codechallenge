package momenso.brasilct.codechallenge.dao;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import momenso.brasilct.codechallenge.Util;
import momenso.brasilct.codechallenge.domain.Line;
import momenso.brasilct.codechallenge.domain.RoutePlan;
import momenso.brasilct.codechallenge.domain.Station;
import momenso.brasilct.codechallenge.domain.Platform;
import momenso.brasilct.codechallenge.domain.StationNode;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;


public class GraphDb {
	private static String dbPath = "var/graphdb";
	private static GraphDatabaseService graphDb;
	private static GraphDb instance;

	private GraphDb() {
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbPath);
	}
	
	public static GraphDb getInstance() {
		if (instance == null) {
			clear();
			instance = new GraphDb();
			instance.index();
			instance.load();
		}
		
		return instance;
	}
	
	public static void clear() {
		Util.deleteFileOrDirectory(new File(dbPath));
	}
	
	public RoutePlan route(StationNode origin, StationNode destination)
	{		
		Transaction tx = graphDb.beginTx();
		try
		{
			return route(origin.getId(), destination.getId());
		}
		finally {
			tx.close();
		}
	}
	
	public RoutePlan route(long origin, long destination) {
		Transaction tx = graphDb.beginTx();
		try
		{
			Node originNode = graphDb.getNodeById(origin);
			Node destinationNode = graphDb.getNodeById(destination);
			PathFinder<WeightedPath> dijkstra = GraphAlgoFactory.dijkstra(
			        PathExpanders.allTypesAndDirections(), "cost");
			WeightedPath path = dijkstra.findSinglePath(originNode, destinationNode);
			
			RoutePlan result = new RoutePlan(path.nodes(), (int)path.weight()-100);
			return result;
		}
		finally {
			tx.close();
		}
	}
	
	public StationNode getStation(long id) {
		Transaction tx = graphDb.beginTx();
		try
		{
			Node node = graphDb.getNodeById(id);
			StationNode station = new StationNode(
            		Long.valueOf(node.getId()), 
            		node.getProperty("name").toString(),
            		(int) node.getProperty("line"));
			return station;
		} finally {
			tx.close();
		}
	}
		
	public List<StationNode> find(String stationName)
	{
		Label label = DynamicLabel.label("Station");
		Transaction tx = graphDb.beginTx();
		try
		{
			ResourceIterator<Node> stations = graphDb.findNodesByLabelAndProperty(label, "name", stationName).iterator();
		    try
		    {
		        List<StationNode> stationNodes = new ArrayList<StationNode>();
		        while (stations.hasNext()) {
		        	Node node = stations.next();
		            stationNodes.add(new StationNode(
		            		Long.valueOf(node.getId()), 
		            		node.getProperty("name").toString(),
		            		(int) node.getProperty("line")));
		        }

		        return stationNodes;
		    } finally {
		    	stations.close();
		    }
		} finally {
			tx.close();
		}
	}
	
	public void index()
	{
		Transaction tx = graphDb.beginTx();
		IndexDefinition indexDefinition;
		try
		{
		    Schema schema = graphDb.schema();
		    indexDefinition = schema.indexFor(DynamicLabel.label("Station"))
		            .on("name")
		            .create();
		    tx.success();
		}
		finally {
			tx.close();
		}
		
		// wait index to complete
		tx = graphDb.beginTx();
		try
		{
			Schema schema = graphDb.schema();
		    schema.awaitIndexOnline( indexDefinition, 30, TimeUnit.SECONDS );
		}
		finally {
			tx.close();
		}
	}
	
	private Map<Platform, Node> nodes;
	
	private Node assignPlatform(Station station1, Line line, GraphDatabaseService graphDb) {
		Label label = DynamicLabel.label("Platform");
		
		Platform ref1 = new Platform(line.getLine(), station1.getId()); 
		Node node1 = nodes.get(ref1);
		if (node1 == null) node1 = graphDb.createNode(label);
		node1.setProperty("name", station1.getName());
		node1.setProperty("id", station1.getId());
		node1.setProperty("line", line.getLine());
		nodes.put(ref1, node1);
		
		return node1;
	}
	
	public void load() 
	{
		Transaction tx = graphDb.beginTx();
		try
		{
			MapLoader loader = MapLoader.getInstance();
			
			// create stations
			Label label = DynamicLabel.label("Station");
			for (Station station : loader.getStations()) {
				Node node = graphDb.createNode(label);
				node.setProperty("name", station.getName());
				node.setProperty("id", station.getId());
				node.setProperty("line", 0);
				station.setNode(node);
			}
			
			// initialize Node cache
			nodes = new HashMap<Platform, Node>();
			
			for (Line line : loader.getLines()) {
				Station station1 = loader.findStationById(line.getStation1());
				Node node1 = assignPlatform(station1, line, graphDb);

				Station station2 = loader.findStationById(line.getStation2());
				Node node2 = assignPlatform(station2, line, graphDb);
				
				// connection between platforms
				Relationship link = node1.createRelationshipTo(node2, RelationshipTypes.DIRECT);
				link.setProperty("cost", 3);
				
				// connects station1 to its platform
				Relationship link_s1 = node1.createRelationshipTo(station1.getNode(), RelationshipTypes.ACCESS);
				link_s1.setProperty("cost", 50);
				
				// connects station2 to its platform
				Relationship link_s2 = node2.createRelationshipTo(station2.getNode(), RelationshipTypes.ACCESS);
				link_s2.setProperty("cost", 50);
			}
						
			// create transfer link between lines
			Node[] nodeArray = nodes.values().toArray(new Node[0]);
			for (int i = 0; i < nodeArray.length; i++) {
				for (int j = i; j < nodeArray.length; j++) {
					if (i == j) continue;
					if (nodeArray[i].getProperty("id") == nodeArray[j].getProperty("id") &&
						nodeArray[i].getProperty("line") != nodeArray[j].getProperty("line")) {
																		
						Relationship link = nodeArray[i].createRelationshipTo(nodeArray[j], RelationshipTypes.TRANSFER);
						link.setProperty("cost", 12);
					}
				}
			}
			
			tx.success();
		}
		finally
		{
			tx.close();
		}
	}
	
	public static void shutdown() {
		if (graphDb != null) {
			graphDb.shutdown();
		}
	}
	
	public enum RelationshipTypes implements RelationshipType
	{
		ACCESS,
	    DIRECT,
	    TRANSFER
	}
}
