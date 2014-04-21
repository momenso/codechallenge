package momenso.brasilct.codechallenge.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/***
 * Represents a path on the train network map.
 * 
 * @author momenso
 *
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MapPath {

	@XmlElement(name = "stations")
	private List<StationNode> path;
	
	public MapPath() { }
	
	public MapPath(List<StationNode> path) {
		this.path = new ArrayList<StationNode>(path);
	}

	public List<StationNode> getPath() {
		return path;
	}

	public void setPath(List<StationNode> path) {
		this.path = path;
	}
	
}
