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
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType

import javax.ws.rs.core.Response

@Path("/webrates")
@Slf4j
class WebRatesResource {

    private final String NEW_LINE = System.getProperty('line.separator')

    @Autowired
    private Repository repository

    private final formats = [
            'csv' : [separator: ', ', extn: 'csv'],
            'psv' : [separator: '| ', extn: 'psv']
        ].withDefault {
            [separator: '\t', extn: 'txt']
        }

    @GET
    @Path('/health')
    @Produces(MediaType.APPLICATION_JSON)
    public String ping() {
        "{greet: 'hello from webrate resource'}"
    }

    @GET
    public Response findAll(@DefaultValue('')
                                @QueryParam('format') String format) {
        findAll('', format)
    }

    @GET
    @Path('/{propertyCode}')
    public Response findAll(@DefaultValue('')
                            @PathParam('propertyCode') String propertyCode,
                            @DefaultValue('')
                            @QueryParam('format') String format) {

        log.info("findAll: propertyCode = $propertyCode, format = $format")
        def rates = propertyCode? repository.findAll(['PropertyCode': propertyCode]): repository.findAll()
        if (format) {
            def filename = "${(propertyCode?: 'AllProperties')}-${System.currentTimeMillis()}"
            def formatDetails = formats[format]
            File file = writeTo(filename, rates, formatDetails.separator)
            Response.ResponseBuilder response = Response.ok(file)
            response.header('Content-Disposition', "attachment; filename='${filename}.${formatDetails.extn}'")
                    .build()
        } else {
            Response.ResponseBuilder response = Response.ok(rates)
            response.header('Content-Type', 'application/json')
                    .build()
        }
    }

    private File writeTo(String filename, List<Map> rates, String separator) {
        File file = new File(filename)
        file.withWriter { writer ->
            rates.eachWithIndex { Map rate, idx ->
                if (idx == 0) {
                    def header = rate.keySet()
                    write(writer, header, separator)
                }
                def data = rate.values()
                write(writer, data, separator)
            }
        }
        file
    }

    private def write(writer, List row, String separator) {
        writer.write(row.join(separator))
        writer.write(NEW_LINE)
    }
}
