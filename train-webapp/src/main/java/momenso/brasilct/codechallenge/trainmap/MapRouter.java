package momenso.brasilct.codechallenge.trainmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * Generates routes between two vertexes (train stations) on a undirected graph
 * based on Dijkstra's algorithm that considers a time penalty when switching
 * lines.
 * 
 * @author momenso
 * 
 */
public class MapRouter {
	private final List<Edge> edges;
	private Set<Vertex> settledNodes;
	private Set<Vertex> unSettledNodes;
	private Map<Vertex, Vertex> predecessors;
	private Map<Vertex, Integer> distance;

	public MapRouter(Graph graph) {
		this.edges = new ArrayList<Edge>(graph.getEdges());
	}

	public void execute(Vertex source) {
		settledNodes = new HashSet<Vertex>();
		unSettledNodes = new HashSet<Vertex>();
		distance = new HashMap<Vertex, Integer>();
		predecessors = new HashMap<Vertex, Vertex>();
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			Vertex node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	private void findMinimalDistances(Vertex node) {
		List<Vertex> adjacentNodes = getNeighbors(node);
		Edge previous = null;
		for (Vertex target : adjacentNodes) {
			int time = getShortestDistance(node) + getDistance(node, target, previous);
			if (getShortestDistance(target) > time) {
				distance.put(target, time);
				predecessors.put(target, node);
				unSettledNodes.add(target);

				previous = getEdge(target, node);
			}
		}
	}

	private Edge getEdge(Vertex a, Vertex b) {
		for (Edge edge : edges) {
			if ((edge.getDestination().equals(a) && edge.getSource().equals(b)) || 
			    (edge.getDestination().equals(b) && edge.getSource().equals(a))) {
				return edge;
			}
		}

		return null;
	}

	private int getDistance(Vertex node, Vertex target, Edge previous) {
		for (Edge edge : edges) {
			if ((edge.getSource().equals(node) && edge.getDestination().equals(target)) || 
				(edge.getDestination().equals(node) && edge.getSource().equals(target))) {

				// adds time delay when switching between different train lines
				int delay = (previous != null && edge.getLine() != previous.getLine()) ? 4 : 1;
				return edge.getWeight() * delay;
			}
		}
		throw new RuntimeException("Should not happen");
	}

	private List<Vertex> getNeighbors(Vertex node) {
		List<Vertex> neighbors = new ArrayList<Vertex>();
		for (Edge edge : edges) {
			if (edge.getSource().equals(node) && !isSettled(edge.getDestination())) {
				neighbors.add(edge.getDestination());
			} else if ((edge.getDestination().equals(node) && !isSettled(edge.getSource()))) {
				neighbors.add(edge.getSource());
			}
		}
		return neighbors;
	}

	private Vertex getMinimum(Set<Vertex> vertexes) {
		Vertex minimum = null;
		for (Vertex vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	private boolean isSettled(Vertex vertex) {
		return settledNodes.contains(vertex);
	}

	private int getShortestDistance(Vertex destination) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	public List<Vertex> getPath(Vertex target) {
		List<Vertex> path = new ArrayList<Vertex>();
		Vertex step = target;
		// check if a path exists
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		
		// fix ordering
		Collections.reverse(path);
		return path;
	}

	public int getTime(Vertex destination) {
		return distance.get(destination);
	}
}
