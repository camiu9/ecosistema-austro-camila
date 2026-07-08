package ec.com.austro.orchestrator.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class CreditEvaluationRepository implements PanacheRepositoryBase<CreditEvaluationEntity, UUID> {
}

