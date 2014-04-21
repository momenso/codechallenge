package momenso.brasilct.codechallenge.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/***
 * Travel time
 * @author momenso
 *
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TravelTime {

	private int minutes;
	
	public TravelTime() { }
	
	public TravelTime(int minutes) {
		this.setMinutes(minutes);
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
}
