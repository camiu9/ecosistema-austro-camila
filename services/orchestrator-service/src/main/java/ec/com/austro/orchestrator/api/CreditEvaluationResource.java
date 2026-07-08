package ec.com.austro.orchestrator.api;

import ec.com.austro.orchestrator.domain.CreditEvaluationService;
import ec.com.austro.orchestrator.dto.CreditEvaluationRequest;
import ec.com.austro.orchestrator.dto.CreditEvaluationResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/v1/credit-evaluations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CreditEvaluationResource {

    private final CreditEvaluationService creditEvaluationService;

    public CreditEvaluationResource(CreditEvaluationService creditEvaluationService) {
        this.creditEvaluationService = creditEvaluationService;
    }

    /**
     * Ejecuta una evaluacion crediticia y persiste el resultado final.
     *
     * @param request datos de entrada validados para la evaluacion.
     * @return evaluacion creada con estado aprobado o rechazado.
     */
    @POST
    public Response create(@Valid CreditEvaluationRequest request) {
        CreditEvaluationResponse response = creditEvaluationService.evaluate(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    /**
     * Lista las evaluaciones registradas, ordenadas de la mas reciente a la mas antigua.
     *
     * @return historial completo de evaluaciones.
     */
    @GET
    public List<CreditEvaluationResponse> list() {
        return creditEvaluationService.list();
    }
}

