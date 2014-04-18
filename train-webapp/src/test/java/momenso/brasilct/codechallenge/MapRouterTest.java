package momenso.brasilct.codechallenge;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import momenso.brasilct.codechallenge.trainmap.Graph;
import momenso.brasilct.codechallenge.trainmap.MapLoader;
import momenso.brasilct.codechallenge.trainmap.MapRouter;
import momenso.brasilct.codechallenge.trainmap.Vertex;

import org.junit.Test;

public class MapRouterTest extends TestCase {

	@Test
	public void testDirectShortRouting() {
		MapLoader loader = MapLoader.getInstance();
		Graph graph = loader.getGraph();
		MapRouter router = new MapRouter(graph);
		Vertex origin = loader.findVertexByName("Liverpool Street");
		Vertex destination = loader.findVertexByName("Mile End");
		router.execute(origin);
		List<Vertex> path = router.getPath(destination);

		List<Vertex> expected = new ArrayList<Vertex>();
		expected.add(new Vertex("156", "Liverpool Street"));
		expected.add(new Vertex("24", "Bethnal Green"));
		expected.add(new Vertex("164", "Mile End"));
		assertThat(path, is(expected));
		
		assertEquals(6, router.getTime(destination));
	}

	@Test
	public void testDirectLongRouting() {
		MapLoader loader = MapLoader.getInstance();
		Graph graph = loader.getGraph();
		MapRouter router = new MapRouter(graph);
		Vertex origin = loader.findVertexByName("Wimbledon");
		Vertex destination = loader.findVertexByName("High Street Kensington");
		router.execute(origin);
		List<Vertex> path = router.getPath(destination);
		
		assertEquals(10, path.size()); // number of stations
		assertEquals(27, router.getTime(destination)); // total time
	}
	
	@Test
	public void testNonDirectRouting() {
		MapLoader loader = MapLoader.getInstance();
		Graph graph = loader.getGraph();
		MapRouter router = new MapRouter(graph);
		Vertex origin = loader.findVertexByName("Baker Street");
		Vertex destination = loader.findVertexByName("Green Park");
		router.execute(origin);
		List<Vertex> path = router.getPath(destination);

		List<Vertex> expected = new ArrayList<Vertex>();
		expected.add(new Vertex("11", "Baker Street"));
		expected.add(new Vertex("212", "Regent's Park"));
		expected.add(new Vertex("192", "Oxford Circus"));
		expected.add(new Vertex("107", "Green Park"));
		assertThat(path, is(expected));
		
		assertEquals(18, router.getTime(destination));
	}
}
