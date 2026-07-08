package ec.com.austro.orchestrator.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ec.com.austro.orchestrator.risk.DebtResponse;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class CreditEvaluationServiceTest {

    private final CreditEvaluationService service = new CreditEvaluationService(null, null);

    @Test
    void shouldApproveWhenScoreAndDebtCapacityPass() {
        var status = service.decide(
                90,
                new BigDecimal("100.00"),
                new BigDecimal("200.00"),
                new BigDecimal("1000.00"));

        assertThat(status).isEqualTo(CreditDecisionStatus.APPROVED);
    }

    @Test
    void shouldRejectWhenScoreIsTooLow() {
        var status = service.decide(
                70,
                BigDecimal.ZERO,
                new BigDecimal("100.00"),
                new BigDecimal("1000.00"));

        assertThat(status).isEqualTo(CreditDecisionStatus.REJECTED);
    }

    @Test
    void shouldSumMonthlyDebt() {
        var total = service.calculateMonthlyDebt(List.of(
                new DebtResponse("1", new BigDecimal("100.10")),
                new DebtResponse("2", new BigDecimal("200.20"))));

        assertThat(total).isEqualByComparingTo("300.30");
    }
}
