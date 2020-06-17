package uk.ac.ebi.spot.gwas.deposition.audit.service.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.audit.config.AuditEmailConfig;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.MailConstants;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.DigestEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.service.AuditEmailService;
import uk.ac.ebi.spot.gwas.deposition.audit.util.EmailSubmissionObject;
import uk.ac.ebi.spot.gwas.deposition.audit.util.StatsEmailBuilder;
import uk.ac.ebi.spot.gwas.deposition.audit.util.SubmissionDigestCheck;
import uk.ac.ebi.spot.gwas.deposition.messaging.email.EmailBuilder;
import uk.ac.ebi.spot.gwas.deposition.messaging.email.EmailService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuditEmailServiceImpl implements AuditEmailService {

    private static final Logger log = LoggerFactory.getLogger(AuditEmailService.class);

    @Autowired(required = false)
    private EmailService emailService;

    @Autowired
    private AuditEmailConfig auditEmailConfig;

    @Override
    public void sendStatsEmail(DigestEntry digestEntry, Pair<String, String> emailConfig) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(MailConstants.SUBMISSIONS_CREATED, digestEntry.getNoSubmissions());
        metadata.put(MailConstants.VALIDATION_SUCCESSFUL, digestEntry.getNoValidSubmissions());
        metadata.put(MailConstants.VALIDATION_FAILED, digestEntry.getNoFailedSubmissions());
        metadata.put(MailConstants.SUBMISSIONS, new SubmissionDigestCheck(digestEntry.getSubmissions()).getResult());
        metadata.put(MailConstants.VALID_SUBMISSIONS, new SubmissionDigestCheck(digestEntry.getValidSubmissions()).getResult());
        metadata.put(MailConstants.FAILED_SUBMISSIONS, new SubmissionDigestCheck(digestEntry.getFailedSubmissions()).getResult());

        log.info("Email service active: {}", emailService != null);
        log.info("Audit email active: {}", auditEmailConfig.isEmailActive());

        if (emailService != null && auditEmailConfig.isEmailActive()) {
            try {
                EmailBuilder successBuilder = new StatsEmailBuilder(emailConfig.getRight());

                for (String to : auditEmailConfig.getDigestTo()) {
                    emailService.sendMessage(to, emailConfig.getLeft(), successBuilder.getEmailContent(metadata), true);
                }
            } catch (Exception e) {
                log.error("ERROR: {}", e.getMessage(), e);
            }
        }
    }
}
