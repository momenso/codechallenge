package momenso.brasilct.codechallenge;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import momenso.brasilct.codechallenge.dao.GraphDb;
import momenso.brasilct.codechallenge.domain.MapPath;
import momenso.brasilct.codechallenge.domain.RoutePlan;
import momenso.brasilct.codechallenge.domain.StationNode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GraphDbTest extends TestCase {
	
	private GraphDb graphDb;
	
	@Before
	public void setUp() {
		graphDb = GraphDb.getInstance();
	}
	
	@After
	public void cleanUp() {
		GraphDb.shutdown();
	}

	@Test
	public void testDirectShortRouting() {
		StationNode origin = graphDb.find("Liverpool Street").get(0);
		StationNode destination = graphDb.find("Mile End").get(0);
		RoutePlan plan = graphDb.route(origin, destination);
		
		List<StationNode> expected = new ArrayList<StationNode>();
		expected.add(new StationNode(26, "Liverpool Street", 2));
		expected.add(new StationNode(31, "Bethnal Green", 2));
		expected.add(new StationNode(32, "Mile End", 2));
		assertThat(plan.getMapPath().getPath(), is(expected));
		
		assertEquals(6, plan.getTravelTime().getMinutes());
	}

	@Test
	public void testDirectLongRouting() {
		
		long origin = 164; 			// Wimbledon
		long destination = 137; 	// High Street Kensington
		RoutePlan plan = graphDb.route(origin, destination);
						
		List<StationNode> expected = new ArrayList<StationNode>();
		expected.add(new StationNode(164, "Wimbledon", 4));
		expected.add(new StationNode(161, "Wimbledon Park", 4));
		expected.add(new StationNode(143, "Southfields", 4));
		expected.add(new StationNode(141, "East Putney", 4));
		expected.add(new StationNode(142, "Putney Bridge", 4));
		expected.add(new StationNode(149, "Parsons Green", 4));
		expected.add(new StationNode(148, "Fulham Broadway", 4));
		expected.add(new StationNode(139, "West Brompton", 4));
		expected.add(new StationNode(135, "Earl's Court", 4));
		expected.add(new StationNode(137, "High Street Kensington", 4));
		
		MapPath mapPath = plan.getMapPath();
		assertThat(mapPath.getPath(), is(expected));

		assertEquals(10, mapPath.getPath().size()); // number of stations
		assertEquals(27, plan.getTravelTime().getMinutes()); // total time
	}
	
	@Test
	public void testNonDirectRouting() {
		
		long origin = 201; 		// Baker Street
		long destination = 34;	// Marble Arch
		RoutePlan plan = graphDb.route(origin, destination);
						
		List<StationNode> expected = new ArrayList<StationNode>();
		expected.add(new StationNode(201, "Baker Street", 7));
		expected.add(new StationNode(202, "Bond Street", 7));
		expected.add(new StationNode(33, "Bond Street", 2));
		expected.add(new StationNode(34, "Marble Arch", 2));
		assertThat(plan.getMapPath().getPath(), is(expected));
		
		assertEquals(18, plan.getTravelTime().getMinutes());
	}
	
}
