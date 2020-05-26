package uk.ac.ebi.spot.gwas.deposition.audit.scheduler.tasks;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.ebi.spot.gwas.deposition.audit.config.AuditEmailConfig;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.MailConstants;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.DigestEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.*;
import uk.ac.ebi.spot.gwas.deposition.audit.service.AuditEmailService;
import uk.ac.ebi.spot.gwas.deposition.audit.util.DigestProcessor;

import java.util.List;

@Component
public class WeeklyStatsTask {

    private static final Logger log = LoggerFactory.getLogger(WeeklyStatsTask.class);

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

    @Autowired
    private AuditEmailConfig auditEmailConfig;

    public void buildStats() {
        DateTime now = DateTime.now();
        if (now.getDayOfWeek() != 7) {
            log.info("Not Sunday ... not generating weekly digest ...");
            return;
        }
        log.info("Generating weekly submissions digest ...");
        DateTime aWeekAgo = DateTime.now().minusDays(7);

        List<AuditEntry> auditEntryList = auditEntryRepository.findByTimestampAfter(aWeekAgo);
        log.info("Found {} entries.", auditEntryList.size());

        DigestEntry digestEntry = new DigestProcessor(auditEntryList,
                userRepository,
                submissionRepository,
                publicationRepository,
                bodyOfWorkRepository).getDigestEntry();
        digestEntry = digestEntryRepository.insert(digestEntry);
        log.info("Digest entry created: {}", digestEntry.getId());

        log.info("Sending digest email ...");
        auditEmailService.sendStatsEmail(digestEntry, auditEmailConfig.emailConfig(MailConstants.DIGEST_WEEKLY));
    }
}
