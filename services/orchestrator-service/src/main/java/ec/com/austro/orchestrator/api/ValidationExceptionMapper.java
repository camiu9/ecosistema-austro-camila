package ec.com.austro.orchestrator.api;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final Map<String, String> FIELD_NAMES = new LinkedHashMap<>();

    static {
        FIELD_NAMES.put("cedula", "Cedula");
        FIELD_NAMES.put("requestedAmount", "Monto solicitado");
        FIELD_NAMES.put("termYears", "Tiempo en anios");
        FIELD_NAMES.put("salary", "Salario");
    }

    /**
     * Transforma errores de validacion en una respuesta HTTP 400 consistente.
     *
     * @param exception excepcion lanzada por Jakarta Validation.
     * @return respuesta con detalle controlado de campos invalidos.
     */
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<String> details = exception.getConstraintViolations().stream()
                .map(violation -> readableFieldName(violation.getPropertyPath().toString())
                        + ": " + violation.getMessage())
                .sorted()
                .toList();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiError.of("VALIDATION_ERROR", "La solicitud contiene datos invalidos", details))
                .build();
    }

    /**
     * Convierte una ruta tecnica de Jakarta Validation en una etiqueta entendible para el usuario.
     *
     * @param propertyPath ruta tecnica reportada por el validador.
     * @return nombre funcional del campo.
     */
    private String readableFieldName(String propertyPath) {
        return FIELD_NAMES.entrySet().stream()
                .filter(entry -> propertyPath.endsWith(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("Solicitud");
    }
}
