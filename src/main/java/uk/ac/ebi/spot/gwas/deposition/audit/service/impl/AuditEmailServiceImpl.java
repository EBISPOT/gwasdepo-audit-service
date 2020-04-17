package uk.ac.ebi.spot.gwas.deposition.audit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.audit.config.AuditEmailConfig;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.MailConstants;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.DigestEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.service.AuditEmailService;
import uk.ac.ebi.spot.gwas.deposition.audit.util.StatsEmailBuilder;
import uk.ac.ebi.spot.gwas.deposition.messaging.email.EmailBuilder;
import uk.ac.ebi.spot.gwas.deposition.messaging.email.EmailService;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuditEmailServiceImpl implements AuditEmailService {

    @Autowired(required = false)
    private EmailService emailService;

    @Autowired
    private AuditEmailConfig auditEmailConfig;

    @Override
    public void sendStatsEmail(DigestEntry digestEntry) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(MailConstants.SUBMISSIONS_CREATED, digestEntry.getNoSubmissions());
        metadata.put(MailConstants.VALIDATION_SUCCESSFUL, digestEntry.getNoValidSubmissions());
        metadata.put(MailConstants.VALIDATION_FAILED, digestEntry.getNoFailedSubmissions());
        metadata.put(MailConstants.SUBMISSIONS, digestEntry.getSubmissions());

        if (emailService != null && auditEmailConfig.isEmailActive()) {
            EmailBuilder successBuilder = new StatsEmailBuilder(auditEmailConfig.getDigestEmail());

            for (String to : auditEmailConfig.getDigestTo()) {
                emailService.sendMessage(to, auditEmailConfig.getDigestSubject(), successBuilder.getEmailContent(metadata));
            }
        }
    }
}
