package momenso.brasilct.codechallenge;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

@ApplicationPath("/")
public class Main extends ResourceConfig {

	public Main() {
		register(MultiPartFeature.class);
		register(FileUpload.class);
		property(ServerProperties.TRACING, "ALL");
	}
}
