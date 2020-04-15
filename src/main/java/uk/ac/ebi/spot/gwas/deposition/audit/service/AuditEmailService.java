package uk.ac.ebi.spot.gwas.deposition.audit.service;

import uk.ac.ebi.spot.gwas.deposition.audit.domain.DigestEntry;

public interface AuditEmailService {

    void sendStatsEmail(DigestEntry digestEntry);

}
