package momenso.brasilct.codechallenge.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.neo4j.graphdb.Node;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RoutePlan {
	
	private TravelTime travelTime;

//	@XmlElementWrapper(name = "stations")
//	@XmlElement(name = "station")
//	private List<Vertex> route = new ArrayList<Vertex>();
	private MapPath mapPath;
	
	public RoutePlan() { }
	
	public RoutePlan(Iterable<Node> route, int time) {
		List<Vertex> path = new ArrayList<Vertex>();
		for (Node node : route) {
			path.add(new Vertex(
					Long.valueOf(node.getId()), 
					node.getProperty("name").toString(),
					(int) node.getProperty("line")));
		}
		this.setMapPath(new MapPath(path));
		this.travelTime = new TravelTime(time);
	}

	public TravelTime getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(TravelTime travelTime) {
		this.travelTime = travelTime;
	}

	public MapPath getMapPath() {
		return mapPath;
	}

	public void setMapPath(MapPath mapPath) {
		this.mapPath = mapPath;
	}
}
