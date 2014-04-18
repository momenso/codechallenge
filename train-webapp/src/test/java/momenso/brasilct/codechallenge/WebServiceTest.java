package momenso.brasilct.codechallenge;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

import momenso.brasilct.codechallenge.trainmap.RoutePlan;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class WebServiceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(WebService.class);
    }

    @Test
    public void testGetIt() {
        final String responseMsg = target().path("train-service").request().get(String.class);

        assertEquals("Hello, London Underground!", responseMsg);
    }
    
    @Test
    public void testPlanner() {
    	final WebTarget target = target("train-service/route");
    	RoutePlan plan = target
    			.queryParam("from", "Holland Park")
    			.queryParam("to", "Canada Water")
    			.request()
    			.get(RoutePlan.class);
    	
    	assertEquals(45, plan.getEstimatedTime());
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void testRouteBetweenNonexistentStations() {
    	exception.expect(BadRequestException.class);
        
    	final WebTarget target = target("train-service/route");
    	target.queryParam("from", "Armenia")
    			.queryParam("to", "Barra Funda")
    			.request()
    			.get(String.class);
    }
}
