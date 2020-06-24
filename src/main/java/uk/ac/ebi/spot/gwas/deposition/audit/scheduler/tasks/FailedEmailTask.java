package uk.ac.ebi.spot.gwas.deposition.audit.scheduler.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.ebi.spot.gwas.deposition.audit.config.AuditEmailConfig;
import uk.ac.ebi.spot.gwas.deposition.domain.email.FailedEmail;
import uk.ac.ebi.spot.gwas.deposition.messaging.email.EmailService;
import uk.ac.ebi.spot.gwas.deposition.repository.FailedEmailRepository;

import java.util.List;

@Component
public class FailedEmailTask {

    private static final Logger log = LoggerFactory.getLogger(FailedEmailTask.class);

    @Autowired(required = false)
    private FailedEmailRepository failedEmailRepository;

    @Autowired(required = false)
    private EmailService emailService;

    @Autowired
    private AuditEmailConfig auditEmailConfig;

    public void verifyFailedEmails() {
        log.info("Verifying failed emails ...");
        if (failedEmailRepository != null && emailService != null && auditEmailConfig.isEmailActive()) {
            List<FailedEmail> failedEmails = failedEmailRepository.findAll();

            log.info("Found {} failed emails", failedEmails.size());
            for (FailedEmail failedEmail : failedEmails) {
                log.info("Resending failed email: {}", failedEmail.getId());
                emailService.resendFailedMessage(failedEmail);
            }
        }
    }
}
