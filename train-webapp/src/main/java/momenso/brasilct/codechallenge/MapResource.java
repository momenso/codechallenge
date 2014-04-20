package momenso.brasilct.codechallenge;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import momenso.brasilct.codechallenge.trainmap.RoutePlan;

@Path("v1")
public class MapResource {
	
	@GET
	@Path("/route/{from}/{to}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response route(@PathParam("from") long from, @PathParam("to") long to) {
		try
		{
			GraphDb graphDb = GraphDb.getInstance();
			RoutePlan path = graphDb.route(from, to);
			
			return Response.status(200).entity(path).build();
		}
		catch (Exception e) {
			return Response
				.status(500)
				.type(MediaType.TEXT_PLAIN)
				.entity(e.getMessage())
				.build();
		}
	}

}
