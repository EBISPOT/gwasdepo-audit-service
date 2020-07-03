package uk.ac.ebi.spot.gwas.deposition.audit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.WeeklyDigestEntry;

public interface WeeklyDigestEntryRepository extends MongoRepository<WeeklyDigestEntry, String> {

}
