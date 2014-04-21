package momenso.brasilct.codechallenge;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

import momenso.brasilct.codechallenge.dao.RoutePlan;
import momenso.brasilct.codechallenge.service.WebService;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class WebServiceTest extends JerseyTest {
	
	private String endPoint = "v1/test";

    @Override
    protected Application configure() {
        return new ResourceConfig(WebService.class);
    }

    @Test
    public void testGetIt() {
        final String responseMsg = target().path(endPoint).request().get(String.class);

        assertEquals("Hello, London Underground!", responseMsg);
    }
    
    @Test
    public void testPlanner() {
    	final WebTarget target = target(endPoint+"/route");
    	RoutePlan plan = target
    			.queryParam("from", "Holland Park")
    			.queryParam("to", "Canada Water")
    			.request()
    			.get(RoutePlan.class);
    	
    	assertEquals(60, plan.getEstimatedTime());
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void testRouteBetweenNonexistentStations() {
    	exception.expect(NotFoundException.class);
        
    	final WebTarget target = target(endPoint+"route");
    	target.queryParam("from", "Armenia")
    			.queryParam("to", "Barra Funda")
    			.request()
    			.get(String.class);
    }
}
