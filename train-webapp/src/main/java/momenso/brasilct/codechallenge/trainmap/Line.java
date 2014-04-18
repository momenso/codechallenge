package momenso.brasilct.codechallenge.trainmap;

/***
 * Bean for a train line segment between two stations of the same line
 * 
 * @author momenso
 *
 */
public class Line {
	private Integer station1;
	private Integer station2;
	private Integer line;

	public Integer getStation1() {
		return station1;
	}

	public void setStation1(Integer station1) {
		this.station1 = station1;
	}

	public Integer getStation2() {
		return station2;
	}

	public void setStation2(Integer station2) {
		this.station2 = station2;
	}

	public Integer getLine() {
		return line;
	}

	public void setLine(Integer line) {
		this.line = line;
	}
	
	@Override
	public String toString() {
		return String.format("%d: (%d, %d)", line, station1, station2);
	}
}
