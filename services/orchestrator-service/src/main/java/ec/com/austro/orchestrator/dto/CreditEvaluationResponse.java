package ec.com.austro.orchestrator.dto;

import ec.com.austro.orchestrator.domain.CreditDecisionStatus;
import ec.com.austro.orchestrator.persistence.CreditEvaluationEntity;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreditEvaluationResponse(
        UUID id,
        String cedula,
        BigDecimal requestedAmount,
        Integer termYears,
        BigDecimal salary,
        Integer score,
        BigDecimal monthlyDebt,
        BigDecimal debtCapacity,
        CreditDecisionStatus status,
        OffsetDateTime createdAt) {

    /**
     * Convierte una entidad persistida en DTO de salida sin exponer detalles de JPA.
     *
     * @param entity entidad de evaluacion.
     * @return respuesta publica de la API.
     */
    public static CreditEvaluationResponse fromEntity(CreditEvaluationEntity entity) {
        return new CreditEvaluationResponse(
                entity.id,
                entity.cedula,
                entity.requestedAmount,
                entity.termYears,
                entity.salary,
                entity.score,
                entity.monthlyDebt,
                entity.debtCapacity,
                entity.status,
                entity.createdAt);
    }
}

