package ec.com.austro.risk.domain;

import ec.com.austro.risk.dto.DebtResponse;
import ec.com.austro.risk.dto.ScoreResponse;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RiskService {

    private final long scoreLatencyMs;
    private final long debtsLatencyMs;

    public RiskService(
            @ConfigProperty(name = "risk.latency.score-ms") long scoreLatencyMs,
            @ConfigProperty(name = "risk.latency.debts-ms") long debtsLatencyMs) {
        this.scoreLatencyMs = scoreLatencyMs;
        this.debtsLatencyMs = debtsLatencyMs;
    }

    /**
     * Calcula un score estable para la misma cedula.
     *
     * @param cedula cedula usada como semilla.
     * @return respuesta con score entre 0 y 100.
     */
    public ScoreResponse getScore(String cedula) {
        sleep(scoreLatencyMs);
        int score = new Random(cedula.hashCode()).nextInt(101);
        return new ScoreResponse(cedula, score);
    }

    /**
     * Genera deudas estables para la misma cedula.
     *
     * @param cedula cedula usada como semilla.
     * @return lista de deudas con mensualidades positivas.
     */
    public List<DebtResponse> getDebts(String cedula) {
        sleep(debtsLatencyMs);
        Random random = new Random((cedula + "-debts").hashCode());
        int debtCount = random.nextInt(4);
        List<DebtResponse> debts = new ArrayList<>();

        for (int index = 0; index < debtCount; index++) {
            BigDecimal monthlyPayment = BigDecimal.valueOf(50 + random.nextDouble(550))
                    .setScale(2, RoundingMode.HALF_UP);
            debts.add(new DebtResponse("DEBT-" + (index + 1), monthlyPayment));
        }

        return debts;
    }

    /**
     * Simula la latencia exigida por el enunciado sin mezclarla con la logica de negocio.
     *
     * @param millis tiempo de espera en milisegundos.
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("La simulacion de latencia fue interrumpida", exception);
        }
    }
}
