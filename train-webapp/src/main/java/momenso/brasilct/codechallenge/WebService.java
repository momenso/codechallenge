package momenso.brasilct.codechallenge;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import momenso.brasilct.codechallenge.trainmap.Graph;
import momenso.brasilct.codechallenge.trainmap.MapLoader;
import momenso.brasilct.codechallenge.trainmap.MapRouter;
import momenso.brasilct.codechallenge.trainmap.RoutePlan;
import momenso.brasilct.codechallenge.trainmap.Vertex;

/**
 * Root resource (exposed at "train-service" path)
 */
@Path("train-service")
public class WebService {

	@GET
	@Path("route")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes()
	public Response route(@QueryParam("from") final String from, @QueryParam("to") final String to) {
		try
		{
			MapLoader mapLoader = MapLoader.getInstance();
			
			Vertex origin = mapLoader.findVertexByName(from);
			Vertex destination = mapLoader.findVertexByName(to);
			
			Graph graph = mapLoader.getGraph();
		    MapRouter router = new MapRouter(graph);
		    router.execute(origin);
		    List<Vertex> path = router.getPath(destination);
			
			//return new RoutePlan(path, router.getTime(destination));
		    return Response.ok(new RoutePlan(path, router.getTime(destination))).build();
		} catch (Exception e) {
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
