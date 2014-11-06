import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider
import org.glassfish.jersey.server.ResourceConfig

import javax.ws.rs.ApplicationPath

@ApplicationPath("ws")
public class StormSpikeOutputWSApplication extends ResourceConfig {
    StormSpikeOutputWSApplication() {
//        packages("org.foo.rest;org.bar.rest")
        packages('ws')
        register(new JacksonJsonProvider())
    }
}