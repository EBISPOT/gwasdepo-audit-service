package uk.ac.ebi.spot.gwas.deposition.audit.service;

import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditEntry;

public interface AuditEntriesService {
    AuditEntry getEntry(String auditEntryId);

    void createEntry(AuditEntry auditEntry);
}
