package ec.com.austro.orchestrator.api;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.List;
import org.jboss.logging.Logger;

@Provider
public class UnhandledExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(UnhandledExceptionMapper.class);

    /**
     * Evita exponer stack traces o detalles internos a los consumidores de la API.
     *
     * @param exception excepcion no controlada.
     * @return respuesta segura y normalizada.
     */
    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof WebApplicationException webException) {
            LOG.warn("Error HTTP controlado en la API", exception);
            return Response.status(webException.getResponse().getStatus())
                    .entity(ApiError.of("HTTP_ERROR", "No fue posible procesar la solicitud", List.of()))
                    .build();
        }

        if (exception instanceof ProcessingException) {
            LOG.error("Fallo de comunicacion con el servicio de riesgos", exception);
            return Response.status(Response.Status.BAD_GATEWAY)
                    .entity(ApiError.of("RISK_SERVICE_UNAVAILABLE", "El servicio de riesgos no esta disponible", List.of()))
                    .build();
        }

        LOG.error("Error inesperado en el orquestador", exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiError.of("INTERNAL_ERROR", "Ocurrio un error inesperado", List.of()))
                .build();
    }
}
