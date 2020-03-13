package uk.ac.ebi.spot.gwas.deposition.audit.service;

import java.util.Map;

public interface AuditEmailService {

    void sendStatsEmail(Map<String, String> metadata);

}
