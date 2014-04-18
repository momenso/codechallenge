package momenso.brasilct.codechallenge;

import junit.framework.TestCase;
import momenso.brasilct.codechallenge.trainmap.MapLoader;

import org.junit.Test;

public class MapLoaderTest extends TestCase {

	@Test
    public void testMapLoading() {
		MapLoader map = MapLoader.getInstance();
		assertEquals(410, map.getLines().size());
		assertEquals(13, map.getRoutes().size());
		assertEquals(306, map.getStations().size());
	}
}
