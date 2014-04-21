package momenso.brasilct.codechallenge.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import momenso.brasilct.codechallenge.dao.GraphDb;
import momenso.brasilct.codechallenge.dao.RoutePlan;
import momenso.brasilct.codechallenge.domain.Vertex;

/**
 * Root resource (exposed at "train-service" path)
 */
@Path("v1/test")
public class WebService {
	
	@GET
	@Path("route")
	@Produces(MediaType.APPLICATION_JSON)
	public Response route(@QueryParam("from") final String from, @QueryParam("to") final String to) {
		try
		{
		    GraphDb graphDb = GraphDb.getInstance();
		    List<Vertex> origin = graphDb.find(from);
		    List<Vertex> destination = graphDb.find(to);
		    
		    if (origin.isEmpty() || destination.isEmpty()) {
		    	return Response
						.status(404)
						.type(MediaType.TEXT_PLAIN)
						.entity("Specified station not found")
						.build();
		    }
		    
		    RoutePlan routePlan = graphDb.route(origin.get(0), destination.get(0));
		    return Response.ok(routePlan).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response
				.status(400)
				.type(MediaType.TEXT_PLAIN)
				.entity(e.getMessage())
				.build();
		}
	}
		
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Hello, London Underground!";
    }
}
