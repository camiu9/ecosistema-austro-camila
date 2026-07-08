package ec.com.austro.orchestrator.dto;

import ec.com.austro.orchestrator.validation.ValidEcuadorianCedula;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreditEvaluationRequest(
        @NotBlank
        @ValidEcuadorianCedula
        String cedula,

        @NotNull
        @DecimalMin(value = "0.01", message = "El monto solicitado debe ser positivo")
        BigDecimal requestedAmount,

        @NotNull
        @Min(value = 1, message = "El plazo debe ser al menos de un anio")
        Integer termYears,

        @NotNull
        @DecimalMin(value = "0.01", message = "El salario debe ser positivo")
        BigDecimal salary) {
}

