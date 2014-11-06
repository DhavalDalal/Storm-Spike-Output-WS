package ws.exception

import groovy.json.JsonBuilder
import groovy.util.logging.Slf4j

import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE
import static javax.ws.rs.core.MediaType.APPLICATION_JSON

@Provider
@Slf4j
class ApplicationExceptionMapper implements ExceptionMapper<Throwable> {

    private final exRespBuilders = [:].withDefault {
        Response.serverError()
    }

    @Override
    Response toResponse(Throwable exception) {
        log.error("Exception : ", exception)
        Response.ResponseBuilder erb = exRespBuilders[exception.getClass().name]
        def message = exception.message
        def entity = new JsonBuilder()
        entity {
            failure message
        }

        erb.header(CONTENT_TYPE, APPLICATION_JSON)
                .entity(entity.content)
                .build()
    }
}
