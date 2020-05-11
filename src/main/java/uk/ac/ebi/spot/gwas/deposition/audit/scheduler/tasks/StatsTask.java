package uk.ac.ebi.spot.gwas.deposition.audit.scheduler.tasks;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.DigestEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.*;
import uk.ac.ebi.spot.gwas.deposition.audit.service.AuditEmailService;
import uk.ac.ebi.spot.gwas.deposition.audit.util.DigestProcessor;

import java.util.List;

@Component
public class StatsTask {

    private static final Logger log = LoggerFactory.getLogger(StatsTask.class);

    @Autowired
    private AuditEntryRepository auditEntryRepository;

    @Autowired
    private DigestEntryRepository digestEntryRepository;

    @Autowired
    private AuditEmailService auditEmailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private BodyOfWorkRepository bodyOfWorkRepository;

    public void buildStats() {
        log.info("Generating daily submissions digest ...");
        DateTime yesterday = DateTime.now().minusDays(1);

        List<AuditEntry> auditEntryList = auditEntryRepository.findByTimestampAfter(yesterday);
        log.info("Found {} entries.", auditEntryList.size());

        if (!auditEntryList.isEmpty()) {
            DigestEntry digestEntry = new DigestProcessor(auditEntryList,
                    userRepository,
                    submissionRepository,
                    publicationRepository,
                    bodyOfWorkRepository).getDigestEntry();
            digestEntry = digestEntryRepository.insert(digestEntry);
            log.info("Digest entry created: {}", digestEntry.getId());

            if (digestEntry.getNoFailedSubmissions() != 0 || digestEntry.getNoSubmissions() != 0 ||
                    digestEntry.getNoValidSubmissions() != 0) {
                log.info("Sending digest email ...");
                auditEmailService.sendStatsEmail(digestEntry);
            }
        }
    }
}
