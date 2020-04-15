package uk.ac.ebi.spot.gwas.deposition.audit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.audit.config.AuditEmailConfig;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.DigestEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.service.AuditEmailService;
import uk.ac.ebi.spot.gwas.deposition.audit.util.StatsEmailBuilder;
import uk.ac.ebi.spot.gwas.deposition.domain.User;
import uk.ac.ebi.spot.gwas.deposition.messaging.email.EmailBuilder;
import uk.ac.ebi.spot.gwas.deposition.messaging.email.EmailService;

@Service
public class AuditEmailServiceImpl implements AuditEmailService {

    @Autowired(required = false)
    private EmailService emailService;

    @Autowired
    private AuditEmailConfig auditEmailConfig;

    @Override
    public void sendStatsEmail(DigestEntry digestEntry) {
        /*
        User user = userService.getUser(userId);
        metadata.put(MailConstants.USER_NAME, user.getName());

        if (emailService != null && auditEmailConfig.isEmailActive()) {
            EmailBuilder successBuilder = new StatsEmailBuilder(auditServiceConfig.getDigestEmail());
            emailService.sendMessage(user.getEmail(), getSubject(pubmedId), successBuilder.getEmailContent(metadata));
        }
        */
    }
}
