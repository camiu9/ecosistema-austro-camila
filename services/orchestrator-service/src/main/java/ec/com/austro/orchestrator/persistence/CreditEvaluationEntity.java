package ec.com.austro.orchestrator.persistence;

import ec.com.austro.orchestrator.domain.CreditDecisionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "credit_evaluations")
public class CreditEvaluationEntity {

    @Id
    @Column(nullable = false, updatable = false)
    public UUID id;

    @Column(nullable = false, length = 10)
    public String cedula;

    @Column(nullable = false, precision = 14, scale = 2)
    public BigDecimal requestedAmount;

    @Column(nullable = false)
    public Integer termYears;

    @Column(nullable = false, precision = 14, scale = 2)
    public BigDecimal salary;

    @Column(nullable = false)
    public Integer score;

    @Column(nullable = false, precision = 14, scale = 2)
    public BigDecimal monthlyDebt;

    @Column(nullable = false, precision = 14, scale = 2)
    public BigDecimal debtCapacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    public CreditDecisionStatus status;

    @Column(nullable = false)
    public OffsetDateTime createdAt;

    /**
     * Inicializa valores tecnicos antes de persistir la evaluacion.
     */
    @PrePersist
    void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }
}

