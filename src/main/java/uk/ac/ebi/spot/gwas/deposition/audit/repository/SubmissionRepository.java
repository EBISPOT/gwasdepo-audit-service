package uk.ac.ebi.spot.gwas.deposition.audit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;

import java.util.Optional;

public interface SubmissionRepository extends MongoRepository<Submission, String> {

    Optional<Submission> findByIdAndArchived(String id, boolean archived);

    Optional<Submission> findByIdAndArchivedAndCreated_UserId(String id, boolean archived, String userId);
}
