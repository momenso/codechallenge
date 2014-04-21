package momenso.brasilct.codechallenge.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/***
 * Represents a path on the train network map
 * 
 * @author momenso
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MapPath {

	@XmlElement(name = "stations")
	private List<Vertex> path;
	
	public MapPath() { }
	
	public MapPath(List<Vertex> path) {
		this.path = new ArrayList<Vertex>(path);
	}

	public List<Vertex> getPath() {
		return path;
	}

	public void setPath(List<Vertex> path) {
		this.path = path;
	}
	
}
