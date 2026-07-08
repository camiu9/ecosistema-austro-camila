package ec.com.austro.orchestrator.risk;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "risk-service")
@Path("/v1/risks")
@Produces(MediaType.APPLICATION_JSON)
public interface RiskClient {

    /**
     * Consulta el score del solicitante en el servicio externo de riesgos.
     *
     * @param cedula cedula ecuatoriana previamente validada.
     * @return score crediticio.
     */
    @GET
    @Path("/{cedula}/score")
    ScoreResponse getScore(@PathParam("cedula") String cedula);

    /**
     * Consulta las deudas vigentes del solicitante en el servicio externo de riesgos.
     *
     * @param cedula cedula ecuatoriana previamente validada.
     * @return lista de deudas.
     */
    @GET
    @Path("/{cedula}/debts")
    List<DebtResponse> getDebts(@PathParam("cedula") String cedula);
}

