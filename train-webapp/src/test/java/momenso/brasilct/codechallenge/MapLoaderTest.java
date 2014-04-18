package momenso.brasilct.codechallenge;

import junit.framework.TestCase;
import momenso.brasilct.codechallenge.trainmap.Graph;
import momenso.brasilct.codechallenge.trainmap.MapLoader;
import momenso.brasilct.codechallenge.trainmap.Vertex;

import org.junit.Test;

public class MapLoaderTest extends TestCase {

	@Test
    public void testMapLoading() {
		MapLoader map = MapLoader.getInstance();
		assertEquals(410, map.getLines().size());
		assertEquals(13, map.getRoutes().size());
		assertEquals(306, map.getStations().size());
	}
	
	@Test
	public void testSpecificStation() {
		MapLoader map = MapLoader.getInstance();
		Vertex target = map.findVertexByName("Neasden");
		assertNotNull(target);
		assertEquals("Neasden", target.getName());
		assertEquals("172", target.getId());
	}
	
	@Test
	public void testGetGraph() {
		MapLoader map = MapLoader.getInstance();
		Graph graph = map.getGraph();
		assertNotNull(graph);
	}
	
	@Test
	public void testGraphLoad() {
		MapLoader map = MapLoader.getInstance();
		Graph graph = map.getGraph();
		assertNotNull(graph);
		assertEquals(410,graph.getEdges().size());
		assertEquals(306,graph.getVertexes().size());
	}
	
}
