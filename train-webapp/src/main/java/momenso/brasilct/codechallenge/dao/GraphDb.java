package momenso.brasilct.codechallenge.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import momenso.brasilct.codechallenge.domain.Line;
import momenso.brasilct.codechallenge.domain.RoutePlan;
import momenso.brasilct.codechallenge.domain.Station;
import momenso.brasilct.codechallenge.domain.StationRef;
import momenso.brasilct.codechallenge.domain.Vertex;

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
			instance = new GraphDb();
		}
		
		return instance;
	}
	
	public static void clear() {
		deleteFileOrDirectory(new File(dbPath));
	}
	
	private static void deleteFileOrDirectory(final File file) {
	    if (file.exists()) {
	        if (file.isDirectory()) {
	            for (File child : file.listFiles()) {
	                deleteFileOrDirectory(child);
	            }
	        }
	        file.delete();
	    }
	}
	
	public RoutePlan route(Vertex origin, Vertex destination)
	{		
		Transaction tx = graphDb.beginTx();
		try
		{
			PathFinder<WeightedPath> dijkstra = GraphAlgoFactory.dijkstra(
			        PathExpanders.allTypesAndDirections(), "cost");
			Node originNode = graphDb.getNodeById(origin.getId());
			Node destinationNode = graphDb.getNodeById(destination.getId());
			WeightedPath path = dijkstra.findSinglePath(originNode, destinationNode);
			
			RoutePlan result = new RoutePlan(path.nodes(), (int)path.weight());
			return result;
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
			
			RoutePlan result = new RoutePlan(path.nodes(), (int)path.weight());
			return result;
		}
		finally {
			tx.close();
		}
	}
		
	public List<Vertex> find(String stationName)
	{
		Label label = DynamicLabel.label("Station");
		Transaction tx = graphDb.beginTx();
		try
		{
			ResourceIterator<Node> stations = graphDb.findNodesByLabelAndProperty(label, "name", stationName).iterator();
		    try
		    {
		        List<Vertex> stationNodes = new ArrayList<Vertex>();
		        while (stations.hasNext()) {
		        	Node node = stations.next();
		            stationNodes.add(new Vertex(
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
	
	private Map<StationRef, Node> nodes;
	
	private Node assignStation(Station station1, Line line, GraphDatabaseService graphDb) {
		Label label = DynamicLabel.label("Station");
		
		StationRef ref1 = new StationRef(line.getLine(), station1.getId()); 
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
			// initialize Node cache
			nodes = new HashMap<StationRef, Node>();
			
			MapLoader loader = MapLoader.getInstance();
			for (Line line : loader.getLines()) {
				Station station1 = loader.findStationById(line.getStation1());
				Node node1 = assignStation(station1, line, graphDb);

				Station station2 = loader.findStationById(line.getStation2());
				Node node2 = assignStation(station2, line, graphDb);
				
				Relationship link = node1.createRelationshipTo(node2, RelationshipTypes.DIRECT);
				link.setProperty("cost", 3);
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
	    DIRECT,
	    TRANSFER
	}
}
