package ec.com.austro.risk.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class RiskServiceTest {

    @Inject
    RiskService riskService;

    @Test
    void shouldReturnStableScoreForSameCedula() {
        var first = riskService.getScore("1710034065");
        var second = riskService.getScore("1710034065");

        assertThat(first.score()).isBetween(0, 100);
        assertThat(first.score()).isEqualTo(second.score());
    }

    @Test
    void shouldReturnNonNegativeMonthlyDebts() {
        var debts = riskService.getDebts("1710034065");

        assertThat(debts).allSatisfy(debt ->
                assertThat(debt.monthlyPayment().signum()).isGreaterThanOrEqualTo(0));
    }
}

