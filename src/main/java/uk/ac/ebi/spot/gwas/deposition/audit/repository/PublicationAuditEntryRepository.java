package uk.ac.ebi.spot.gwas.deposition.audit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.PublicationAuditEntry;

public interface PublicationAuditEntryRepository extends MongoRepository<PublicationAuditEntry, String> {

      Page<PublicationAuditEntry> findByPublicationId(String pubId, Pageable pageable);
}
