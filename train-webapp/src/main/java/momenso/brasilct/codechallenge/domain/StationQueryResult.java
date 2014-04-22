package momenso.brasilct.codechallenge.domain;

import java.util.List;

public class StationQueryResult {

	private List<StationNode> stations;

	public StationQueryResult() { }
	
	public StationQueryResult(List<StationNode> stations) {
		this.stations = stations;
	}
	
	public List<StationNode> getStations() {
		return stations;
	}

	public void setStations(List<StationNode> stations) {
		this.stations = stations;
	}
	
}
