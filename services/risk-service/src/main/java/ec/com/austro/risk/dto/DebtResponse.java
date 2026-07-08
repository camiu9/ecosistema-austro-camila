package ec.com.austro.risk.dto;

import java.math.BigDecimal;

public record DebtResponse(String id, BigDecimal monthlyPayment) {
}

