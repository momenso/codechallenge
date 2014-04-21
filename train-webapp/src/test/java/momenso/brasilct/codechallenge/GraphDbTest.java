package momenso.brasilct.codechallenge;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import momenso.brasilct.codechallenge.dao.GraphDb;
import momenso.brasilct.codechallenge.domain.RoutePlan;
import momenso.brasilct.codechallenge.domain.Vertex;

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
		Vertex origin = graphDb.find("Liverpool Street").get(0);
		Vertex destination = graphDb.find("Mile End").get(0);
		RoutePlan plan = graphDb.route(origin, destination);
		
		List<Vertex> expected = new ArrayList<Vertex>();
		expected.add(new Vertex(26, "Liverpool Street", 2));
		expected.add(new Vertex(31, "Bethnal Green", 2));
		expected.add(new Vertex(32, "Mile End", 2));
		assertThat(plan.getRoute(), is(expected));
		
		assertEquals(6, plan.getEstimatedTime());
	}

	@Test
	public void testDirectLongRouting() {
		
		long origin = 164; 			// Wimbledon
		long destination = 137; 	// High Street Kensington
		RoutePlan plan = graphDb.route(origin, destination);
						
		List<Vertex> expected = new ArrayList<Vertex>();
		expected.add(new Vertex(164, "Wimbledon", 4));
		expected.add(new Vertex(161, "Wimbledon Park", 4));
		expected.add(new Vertex(143, "Southfields", 4));
		expected.add(new Vertex(141, "East Putney", 4));
		expected.add(new Vertex(142, "Putney Bridge", 4));
		expected.add(new Vertex(149, "Parsons Green", 4));
		expected.add(new Vertex(148, "Fulham Broadway", 4));
		expected.add(new Vertex(139, "West Brompton", 4));
		expected.add(new Vertex(135, "Earl's Court", 4));
		expected.add(new Vertex(137, "High Street Kensington", 4));
		assertThat(plan.getRoute(), is(expected));

		assertEquals(10, plan.getRoute().size()); // number of stations
		assertEquals(27, plan.getEstimatedTime()); // total time
	}
	
	@Test
	public void testNonDirectRouting() {
		
		long origin = 201; 		// Baker Street
		long destination = 34;	// Marble Arch
		RoutePlan plan = graphDb.route(origin, destination);
						
		List<Vertex> expected = new ArrayList<Vertex>();
		expected.add(new Vertex(201, "Baker Street", 7));
		expected.add(new Vertex(202, "Bond Street", 7));
		expected.add(new Vertex(33, "Bond Street", 2));
		expected.add(new Vertex(34, "Marble Arch", 2));
		assertThat(plan.getRoute(), is(expected));
		
		assertEquals(18, plan.getEstimatedTime());
	}
	
}
