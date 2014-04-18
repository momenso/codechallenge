package momenso.brasilct.codechallenge.trainmap;

/***
 * Bean representation of train station
 * 
 * @author momenso
 *
 */
public class Station {
	private Integer id;
	private Double latitude;
	private Double longitude;
	private String name;
	private String display_name;
	private Double zone;
	private Integer total_lines;
	private Integer rail;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public Double getZone() {
		return zone;
	}

	public void setZone(Double zone) {
		this.zone = zone;
	}

	public Integer getTotal_lines() {
		return total_lines;
	}

	public void setTotal_lines(Integer total_lines) {
		this.total_lines = total_lines;
	}

	public Integer getRail() {
		return rail;
	}

	public void setRail(Integer rail) {
		this.rail = rail;
	}
	
	@Override
	public String toString() {
		return String.format("%d-%s", id, name);
	}
}
