import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider
import org.glassfish.jersey.server.ResourceConfig

import javax.ws.rs.ApplicationPath

@ApplicationPath("ws")
public class Application extends ResourceConfig {
    Application() {
        packages('ws')
        register(new JacksonJsonProvider())
    }
}