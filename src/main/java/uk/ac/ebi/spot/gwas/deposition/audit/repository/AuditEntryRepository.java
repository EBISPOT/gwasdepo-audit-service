package uk.ac.ebi.spot.gwas.deposition.audit.repository;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditEntry;

import java.util.List;

public interface AuditEntryRepository extends MongoRepository<AuditEntry, String> {

    List<AuditEntry> findByTimestampAfter(DateTime timestamp);

}
