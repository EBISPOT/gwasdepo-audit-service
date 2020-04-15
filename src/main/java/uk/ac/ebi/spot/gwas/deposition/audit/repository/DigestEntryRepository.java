package uk.ac.ebi.spot.gwas.deposition.audit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.DigestEntry;

public interface DigestEntryRepository extends MongoRepository<DigestEntry, String> {

}
