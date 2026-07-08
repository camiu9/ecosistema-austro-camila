package ec.com.austro.orchestrator.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EcuadorianCedulaValidator implements ConstraintValidator<ValidEcuadorianCedula, String> {

    private static final int[] COEFFICIENTS = {2, 1, 2, 1, 2, 1, 2, 1, 2};

    /**
     * Valida una cedula ecuatoriana de persona natural con algoritmo modulo 10.
     *
     * @param cedula valor recibido desde la API.
     * @param context contexto de Jakarta Validation.
     * @return true cuando la cedula tiene provincia, tercer digito y digito verificador validos.
     */
    @Override
    public boolean isValid(String cedula, ConstraintValidatorContext context) {
        if (cedula == null || !cedula.matches("\\d{10}")) {
            return false;
        }

        int province = Integer.parseInt(cedula.substring(0, 2));
        int thirdDigit = Character.getNumericValue(cedula.charAt(2));
        if (province < 1 || province > 24 || thirdDigit >= 6) {
            return false;
        }

        int sum = 0;
        for (int index = 0; index < COEFFICIENTS.length; index++) {
            int value = Character.getNumericValue(cedula.charAt(index)) * COEFFICIENTS[index];
            sum += value >= 10 ? value - 9 : value;
        }

        int expectedVerifier = (10 - (sum % 10)) % 10;
        int actualVerifier = Character.getNumericValue(cedula.charAt(9));
        return expectedVerifier == actualVerifier;
    }
}

