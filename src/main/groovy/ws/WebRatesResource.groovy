package ws

import groovy.util.logging.Slf4j
import net.sf.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import query.MongoRepository
import query.Repository

import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType

import javax.ws.rs.core.Response

@Path("/webrates")
@Slf4j
class WebRatesResource {

    @Autowired
    private Repository repository

    @GET
    @Path("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    public String ping() {
        "{greet: 'hello from webrate resource'}"
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map> findAll() {
        repository.findAll()
    }

    @GET
    @Path("/{propertyCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map> findAll(@DefaultValue('')
                             @PathParam('propertyCode') String propertyCode) {
        log.info("findAll: propertyCode = $propertyCode")
        repository.findAll(['PropertyCode': propertyCode])
    }
}
