package ec.com.austro.orchestrator.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EcuadorianCedulaValidatorTest {

    private final EcuadorianCedulaValidator validator = new EcuadorianCedulaValidator();

    @Test
    void shouldAcceptValidCedula() {
        assertThat(validator.isValid("1710034065", null)).isTrue();
        assertThat(validator.isValid("0926687856", null)).isTrue();
    }

    @Test
    void shouldRejectInvalidCedula() {
        assertThat(validator.isValid("0102030405", null)).isFalse();
        assertThat(validator.isValid("0010034065", null)).isFalse();
        assertThat(validator.isValid("171003406X", null)).isFalse();
    }
}

