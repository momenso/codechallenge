package momenso.brasilct.codechallenge.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.neo4j.graphdb.NotFoundException;

import momenso.brasilct.codechallenge.dao.GraphDb;
import momenso.brasilct.codechallenge.domain.RoutePlan;
import momenso.brasilct.codechallenge.domain.Vertex;

@Path("v1")
public class MapResource {
	
	@GET
	@Path("/station/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response stations(@PathParam("id") long id) {
		try
		{
			GraphDb graphDb = GraphDb.getInstance();
			Vertex station = graphDb.getStation(id);
			
			return Response.status(200).entity(station).build();
		}
		catch (NotFoundException e) {
			return Response.status(404).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
		}
	}
	
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
