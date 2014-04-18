package momenso.brasilct.codechallenge;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
	public RoutePlan route(@QueryParam("from") final String from, @QueryParam("to") final String to) {
		MapLoader mapLoader = MapLoader.getInstance();
		
		Vertex origin = mapLoader.findNodeByName(from);
		Vertex destination = mapLoader.findNodeByName(to);
		
		Graph graph = mapLoader.getGraph();
	    MapRouter router = new MapRouter(graph);
	    router.execute(origin);
	    List<Vertex> path = router.getPath(destination);
	    
		for (Vertex vertex : path) {
			System.out.println(vertex);
		}
		
		return new RoutePlan(path, router.getTime(destination));
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
