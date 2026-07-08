package ec.com.austro.orchestrator.domain;

import ec.com.austro.orchestrator.dto.CreditEvaluationRequest;
import ec.com.austro.orchestrator.dto.CreditEvaluationResponse;
import ec.com.austro.orchestrator.persistence.CreditEvaluationEntity;
import ec.com.austro.orchestrator.persistence.CreditEvaluationRepository;
import ec.com.austro.orchestrator.risk.DebtResponse;
import ec.com.austro.orchestrator.risk.RiskClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class CreditEvaluationService {

    private static final BigDecimal DEBT_CAPACITY_RATE = new BigDecimal("0.40");

    private final RiskClient riskClient;
    private final CreditEvaluationRepository repository;

    public CreditEvaluationService(
            @RestClient RiskClient riskClient,
            CreditEvaluationRepository repository) {
        this.riskClient = riskClient;
        this.repository = repository;
    }

    /**
     * Orquesta la consulta de riesgos, aplica la regla de aprobacion y persiste la evaluacion.
     *
     * @param request solicitud validada desde la API.
     * @return respuesta con datos de riesgo, capacidad y estado final.
     */
    @Transactional
    public CreditEvaluationResponse evaluate(CreditEvaluationRequest request) {
        int score = riskClient.getScore(request.cedula()).score();
        BigDecimal monthlyDebt = calculateMonthlyDebt(riskClient.getDebts(request.cedula()));
        BigDecimal debtCapacity = request.salary().multiply(DEBT_CAPACITY_RATE).setScale(2, RoundingMode.HALF_UP);
        CreditDecisionStatus status = decide(score, monthlyDebt, request.requestedAmount(), debtCapacity);

        CreditEvaluationEntity entity = new CreditEvaluationEntity();
        entity.cedula = request.cedula();
        entity.requestedAmount = request.requestedAmount();
        entity.termYears = request.termYears();
        entity.salary = request.salary();
        entity.score = score;
        entity.monthlyDebt = monthlyDebt;
        entity.debtCapacity = debtCapacity;
        entity.status = status;
        entity.createdAt = OffsetDateTime.now();
        repository.persist(entity);

        return CreditEvaluationResponse.fromEntity(entity);
    }

    /**
     * Recupera el historial persistido de evaluaciones.
     *
     * @return evaluaciones ordenadas por fecha descendente.
     */
    public List<CreditEvaluationResponse> list() {
        return repository.list("order by createdAt desc").stream()
                .map(CreditEvaluationResponse::fromEntity)
                .toList();
    }

    /**
     * Suma las mensualidades reportadas por el servicio de riesgos.
     *
     * @param debts deudas retornadas por el microservicio externo.
     * @return total mensual con dos decimales.
     */
    BigDecimal calculateMonthlyDebt(List<DebtResponse> debts) {
        return debts.stream()
                .map(DebtResponse::monthlyPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Aplica la regla de negocio exigida por el documento de evaluacion.
     *
     * @param score score crediticio del solicitante.
     * @param monthlyDebt deuda mensual actual.
     * @param requestedAmount monto solicitado completo.
     * @param debtCapacity cuarenta por ciento del salario.
     * @return estado final de la solicitud.
     */
    CreditDecisionStatus decide(
            int score,
            BigDecimal monthlyDebt,
            BigDecimal requestedAmount,
            BigDecimal debtCapacity) {
        boolean approved = score > 70
                && monthlyDebt.add(requestedAmount).compareTo(debtCapacity) < 0;
        return approved ? CreditDecisionStatus.APPROVED : CreditDecisionStatus.REJECTED;
    }
}
