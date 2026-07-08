package ec.com.austro.orchestrator.api;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiError(
        String code,
        String message,
        List<String> details,
        OffsetDateTime timestamp) {

    /**
     * Construye una respuesta de error estandar para la API.
     *
     * @param code codigo funcional del error.
     * @param message mensaje seguro para consumidores.
     * @param details detalles de validacion o diagnostico controlado.
     * @return error serializable.
     */
    public static ApiError of(String code, String message, List<String> details) {
        return new ApiError(code, message, details, OffsetDateTime.now());
    }
}

