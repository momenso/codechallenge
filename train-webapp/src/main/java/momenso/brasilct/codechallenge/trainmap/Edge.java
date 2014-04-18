package momenso.brasilct.codechallenge.trainmap;


/***
 * Graph edge
 * 
 * @author momenso
 *
 */
public class Edge {
	private final String id;
	private final Vertex source;
	private final Vertex destination;
	private final int line;
	private final int weight;

	public Edge(String id, Vertex source, Vertex destination, int line, int weight) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.weight = weight;
		this.line = line;
	}

	public String getId() {
		return id;
	}

	public Vertex getDestination() {
		return destination;
	}

	public Vertex getSource() {
		return source;
	}

	public int getWeight() {
		return weight;
	}
	
	public int getLine() {
		return line;
	}

	@Override
	public String toString() {
		return source + " " + destination;
	}
}
