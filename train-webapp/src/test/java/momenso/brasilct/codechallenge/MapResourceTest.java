package momenso.brasilct.codechallenge;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

import momenso.brasilct.codechallenge.domain.MapPath;
import momenso.brasilct.codechallenge.domain.RoutePlan;
import momenso.brasilct.codechallenge.domain.Vertex;
import momenso.brasilct.codechallenge.service.MapResource;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MapResourceTest extends JerseyTest {
	
    @Rule
    public ExpectedException exception = ExpectedException.none();

	@Override
    protected Application configure() {
        return new ResourceConfig(MapResource.class);
    }

    @Test
    public void testStation() {
    	final WebTarget target = target("/v1/station/201");
    	Vertex station = target.request().get(Vertex.class);
    	
    	Vertex expected = new Vertex(201, "Baker Street", 7);
    	assertEquals(expected, station);
    }
        
    @Test
    public void testStationNotFound() {
    	exception.expect(NotFoundException.class);
    	
    	final WebTarget target = target("/v1/station/1000");
    	target.request().get(Vertex.class);    	
    }
    
    @Test
    public void testPath() {
    	final WebTarget target = target("/v1/path/202/34");
    	MapPath path = target.request().get(MapPath.class);
    	
    	List<Vertex> expected = new ArrayList<Vertex>();
		expected.add(new Vertex(202, "Bond Street", 7));
		expected.add(new Vertex(33, "Bond Street", 2));
		expected.add(new Vertex(34, "Marble Arch", 2));
		assertThat(path.getPath(), is(expected));
    }

    @Test
    public void testPlanner() {
    	final WebTarget target = target("/v1/route/201/34");
    	RoutePlan plan = target.request().get(RoutePlan.class);
    	
//    	List<Vertex> path = plan.getRoute();
//    	System.out.println(path.size());
//    	for (Vertex v : path) {
//    		System.out.println(v);
//    	}
    	
    	List<Vertex> expected = new ArrayList<Vertex>();
		expected.add(new Vertex(201, "Baker Street", 7));
		expected.add(new Vertex(202, "Bond Street", 7));
		expected.add(new Vertex(33, "Bond Street", 2));
		expected.add(new Vertex(34, "Marble Arch", 2));
		assertThat(plan.getRoute(), is(expected));
    	
    	assertEquals(18, plan.getEstimatedTime());
    }
    
}
