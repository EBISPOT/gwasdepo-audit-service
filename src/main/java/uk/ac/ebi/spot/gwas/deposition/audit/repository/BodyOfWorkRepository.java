package uk.ac.ebi.spot.gwas.deposition.audit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.spot.gwas.deposition.domain.BodyOfWork;

import java.util.Optional;

public interface BodyOfWorkRepository extends MongoRepository<BodyOfWork, String> {
    Optional<BodyOfWork> findByBowIdAndArchived(String bowId, boolean archived);
}
