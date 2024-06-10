package uk.ac.ebi.spot.gwas.deposition.audit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.spot.gwas.deposition.audit.PublicationAuditEntryDto;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.PublicationAuditEntry;

public interface PublicationAuditEntryService {

    PublicationAuditEntry createPublicationAuditEntry(PublicationAuditEntry publicationAuditEntry);

    PublicationAuditEntry getAuditEntry(String auditEntryId);


    Page<PublicationAuditEntry> getPublicationAuditEntries(String pubId, Pageable pageable);

    String getPublicationId(PublicationAuditEntryDto publicationAuditEntryDto);

}
