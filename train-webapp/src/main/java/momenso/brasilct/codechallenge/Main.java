package momenso.brasilct.codechallenge;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.ext.ContextResolver;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.TracingConfig;

/***
 * Application resource configuration class
 * 
 * @author momenso
 *
 */
@ApplicationPath("/")
public class Main extends ResourceConfig {

	public Main() {
		register(MultiPartFeature.class);
		register(FileUpload.class);
		register(MoxyJsonFeature.class);
		register(createMoxyJsonResolver());
		property(ServerProperties.TRACING, TracingConfig.ON_DEMAND.name());
	}
	
	public static ContextResolver<MoxyJsonConfig> createMoxyJsonResolver() {
		final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
		Map<String, String> namespacePrefixMapper = new HashMap<String, String>(1);
		namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance","xsi");
		moxyJsonConfig
			.setNamespacePrefixMapper(namespacePrefixMapper)
			.setNamespaceSeparator(':')
			.setIncludeRoot(true);
		return moxyJsonConfig.resolver();
	}
}
