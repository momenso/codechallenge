package momenso.brasilct.codechallenge.domain;

/***
 * Bean for representing a line route
 * 
 * @author momenso
 *
 */
public class Route {
	private Integer line;
	private String name;
	private String colour;
	private String stripe;

	public Integer getLine() {
		return line;
	}

	public void setLine(Integer line) {
		this.line = line;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getStripe() {
		return stripe;
	}

	public void setStripe(String stripe) {
		this.stripe = stripe;
	}

	@Override
	public String toString() {
		return String.format("%d-%s", line, name);
	}
}
