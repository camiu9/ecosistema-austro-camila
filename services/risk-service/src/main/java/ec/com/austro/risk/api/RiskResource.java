package ec.com.austro.risk.api;

import ec.com.austro.risk.domain.RiskService;
import ec.com.austro.risk.dto.DebtResponse;
import ec.com.austro.risk.dto.ScoreResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/v1/risks")
@Produces(MediaType.APPLICATION_JSON)
public class RiskResource {

    private final RiskService riskService;

    public RiskResource(RiskService riskService) {
        this.riskService = riskService;
    }

    /**
     * Obtiene un score crediticio simulado para una cedula.
     *
     * @param cedula identificador ecuatoriano usado como semilla deterministica.
     * @return score entre 0 y 100 con latencia configurada.
     */
    @GET
    @Path("/{cedula}/score")
    public ScoreResponse getScore(
            @PathParam("cedula") @NotBlank @Pattern(regexp = "\\d{10}") String cedula) {
        return riskService.getScore(cedula);
    }

    /**
     * Obtiene deudas simuladas asociadas a una cedula.
     *
     * @param cedula identificador ecuatoriano usado como semilla deterministica.
     * @return lista de deudas y mensualidades con latencia configurada.
     */
    @GET
    @Path("/{cedula}/debts")
    public List<DebtResponse> getDebts(
            @PathParam("cedula") @NotBlank @Pattern(regexp = "\\d{10}") String cedula) {
        return riskService.getDebts(cedula);
    }
}

