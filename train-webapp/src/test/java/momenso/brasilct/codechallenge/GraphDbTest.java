package momenso.brasilct.codechallenge;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import momenso.brasilct.codechallenge.dao.GraphDb;
import momenso.brasilct.codechallenge.domain.MapPath;
import momenso.brasilct.codechallenge.domain.RoutePlan;
import momenso.brasilct.codechallenge.domain.StationNode;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class GraphDbTest {
	
	private static GraphDb graphDb;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		graphDb = GraphDb.getInstance();
	}
		
	@Test
	public void testFindStation() {
		StationNode station = graphDb.find("Bond Street").get(0);
		assertEquals("Bond Street", station.getName());
		assertEquals(0, station.getLine());
	}
	
	@Test
	public void testFindStation2() {
		StationNode station = graphDb.find("Holborn").get(0);
		assertEquals("Holborn", station.getName());
		assertEquals(0, station.getLine());
	}

	@Test
	public void testDirectShortRouting() {
		StationNode origin = graphDb.find("Liverpool Street").get(0);
		StationNode destination = graphDb.find("Mile End").get(0);
		RoutePlan plan = graphDb.route(origin, destination);
		
		List<StationNode> expected = new ArrayList<StationNode>();
		expected.add(new StationNode(332, "Liverpool Street", 2));
		expected.add(new StationNode(337, "Bethnal Green", 2));
		expected.add(new StationNode(338, "Mile End", 2));
		assertThat(plan.getMapPath().getPath(), is(expected));
		
		assertEquals(6, plan.getTravelTime().getMinutes());
	}

	@Test
	public void testDirectLongRouting() {
		
		StationNode origin = graphDb.find("Wimbledon").get(0);
		StationNode destination = graphDb.find("High Street Kensington").get(0);
		RoutePlan plan = graphDb.route(origin, destination);
		
		List<StationNode> expected = new ArrayList<StationNode>();
		expected.add(new StationNode(470, "Wimbledon", 4));
		expected.add(new StationNode(467, "Wimbledon Park", 4));
		expected.add(new StationNode(449, "Southfields", 4));
		expected.add(new StationNode(447, "East Putney", 4));
		expected.add(new StationNode(448, "Putney Bridge", 4));
		expected.add(new StationNode(455, "Parsons Green", 4));
		expected.add(new StationNode(454, "Fulham Broadway", 4));
		expected.add(new StationNode(445, "West Brompton", 4));
		expected.add(new StationNode(441, "Earl's Court", 4));
		expected.add(new StationNode(443, "High Street Kensington", 4));
		
		MapPath mapPath = plan.getMapPath();
		assertThat(mapPath.getPath(), is(expected));

		assertEquals(10, mapPath.getPath().size()); // number of stations
		assertEquals(27, plan.getTravelTime().getMinutes()); // total time
	}

	@Test
	public void testNonDirectRouting() {
		
		StationNode origin = graphDb.find("Baker Street").get(0);
		StationNode destination = graphDb.find("Marble Arch").get(0);
		RoutePlan plan = graphDb.route(origin, destination);
						
		List<StationNode> expected = new ArrayList<StationNode>();
		expected.add(new StationNode(507, "Baker Street", 7));
		expected.add(new StationNode(508, "Bond Street", 7));
		expected.add(new StationNode(339, "Bond Street", 2));
		expected.add(new StationNode(340, "Marble Arch", 2));
		assertThat(plan.getMapPath().getPath(), is(expected));
		
		assertEquals(18, plan.getTravelTime().getMinutes());
	}
	
}
