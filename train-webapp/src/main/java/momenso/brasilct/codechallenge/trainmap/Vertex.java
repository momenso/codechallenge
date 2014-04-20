package momenso.brasilct.codechallenge.trainmap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/***
 * Graph vertex
 * 
 * @author momenso
 *
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Vertex {
	private long id;
	private String name;
	private int line;
	
	public Vertex() { }

	public Vertex(long id, String name, int line) {
		this.id = id;
		this.name = name;
		this.setLine(line);
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s-%s <%d>", id, name, line);
	}
}
