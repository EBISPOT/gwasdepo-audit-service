package uk.ac.ebi.spot.gwas.deposition.audit.scheduler.tasks;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.ebi.spot.gwas.deposition.audit.config.AuditEmailConfig;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.MailConstants;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.WeeklyDigestEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.AuditEntryRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.PublicationRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.UserRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.WeeklyDigestEntryRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.service.AuditEmailService;
import uk.ac.ebi.spot.gwas.deposition.audit.util.WeeklyDigestProcessor;

import java.util.List;

@Component
public class WeeklyStatsTask {

    private static final Logger log = LoggerFactory.getLogger(WeeklyStatsTask.class);

    @Autowired
    private AuditEntryRepository auditEntryRepository;

    @Autowired
    private WeeklyDigestEntryRepository weeklyDigestEntryRepository;

    @Autowired
    private AuditEmailService auditEmailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private AuditEmailConfig auditEmailConfig;

    public void buildStats(boolean isTest) {
        DateTime now = DateTime.now();
        if (!isTest && now.getDayOfWeek() != 7) {
            log.info("Not Sunday ... not generating weekly digest ...");
            return;
        }
        log.info("Generating weekly submissions digest ...");
        DateTime aWeekAgo = DateTime.now().minusDays(7);

        List<AuditEntry> auditEntryList = auditEntryRepository.findByTimestampAfter(aWeekAgo);
        log.info("Found {} entries.", auditEntryList.size());

        WeeklyDigestEntry digestEntry = new WeeklyDigestProcessor(auditEntryList,
                userRepository, publicationRepository).getWeeklyDigestEntry();
        digestEntry = weeklyDigestEntryRepository.insert(digestEntry);
        log.info("Digest entry created: {}", digestEntry.getId());

        log.info("Sending digest email ...");
        auditEmailService.sendWeeklyStatsEmail(digestEntry, auditEmailConfig.emailConfig(MailConstants.DIGEST_WEEKLY));
    }
}
