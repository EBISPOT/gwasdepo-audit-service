package uk.ac.ebi.spot.gwas.deposition.audit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuditEmailConfig {

    @Value("${audit-emails.active:false}")
    private boolean emailActive;

    @Value("${audit-emails.digest.content:#{NULL}}")
    private String digestEmail;

    @Value("${audit-emails.digest.to:#{NULL}}")
    private String digestTo;

    @Value("${audit-emails.digest.subject:#{NULL}}")
    private String digestSubject;

    public boolean isEmailActive() {
        return emailActive;
    }

    public String getDigestEmail() {
        return digestEmail;
    }

    public String getDigestSubject() {
        return digestSubject;
    }

    public List<String> getDigestTo() {
        List<String> emails = new ArrayList<>();
        if (digestTo != null) {
            String[] parts = digestTo.split(",");
            for (String part : parts) {
                part = part.trim();
                if (!part.equals("")) {
                    emails.add(part);
                }
            }
        }
        return emails;
    }
}
