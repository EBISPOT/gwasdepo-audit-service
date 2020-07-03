package uk.ac.ebi.spot.gwas.deposition.audit.service;

import org.apache.commons.lang3.tuple.Pair;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.DigestEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.WeeklyDigestEntry;

public interface AuditEmailService {

    void sendStatsEmail(DigestEntry digestEntry, Pair<String, String> emailConfig);

    void sendWeeklyStatsEmail(WeeklyDigestEntry digestEntry, Pair<String, String> emailConfig);

}
