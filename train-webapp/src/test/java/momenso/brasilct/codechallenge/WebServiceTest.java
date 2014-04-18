package momenso.brasilct.codechallenge;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import momenso.brasilct.codechallenge.WebService;
import momenso.brasilct.codechallenge.trainmap.RoutePlan;
import momenso.brasilct.codechallenge.trainmap.Vertex;

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
}
