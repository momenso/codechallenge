package momenso.brasilct.codechallenge.trainmap;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.neo4j.graphdb.Node;

@XmlRootElement
public class RoutePlan {

	private int estimatedTime;
	private List<Vertex> route = new ArrayList<Vertex>();
	
	public RoutePlan() { }
	
	public RoutePlan(Iterable<Node> route, int time) {
		for (Node node : route) {
			this.route.add(new Vertex(
					Long.valueOf(node.getId()), 
					node.getProperty("name").toString(),
					(int) node.getProperty("line")));
		}
		this.setEstimatedTime(time);
	}

	@XmlElement(name="route")
	public List<Vertex> getRoute() {
		return route;
	}

	public void setRoute(List<Vertex> route) {
		this.route.clear();
		this.route.addAll(route);
	}

	public int getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(int time) {
		this.estimatedTime = time;
	}
}
