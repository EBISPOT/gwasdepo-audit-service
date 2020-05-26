package uk.ac.ebi.spot.gwas.deposition.audit.config;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.MailConstants;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuditEmailConfig {

    @Value("${audit-emails.active:false}")
    private boolean emailActive;

    @Value("${audit-emails.digest.daily.content:#{NULL}}")
    private String dailyDigestEmail;

    @Value("${audit-emails.digest.daily.subject:#{NULL}}")
    private String dailyDigestSubject;

    @Value("${audit-emails.digest.weekly.content:#{NULL}}")
    private String weeklyDigestEmail;

    @Value("${audit-emails.digest.weekly.subject:#{NULL}}")
    private String weeklyDigestSubject;

    @Value("${audit-emails.digest.to:#{NULL}}")
    private String digestTo;

    public boolean isEmailActive() {
        return emailActive;
    }

    public String getDailyDigestEmail() {
        return dailyDigestEmail;
    }

    public String getDailyDigestSubject() {
        return dailyDigestSubject;
    }

    public String getWeeklyDigestEmail() {
        return weeklyDigestEmail;
    }

    public String getWeeklyDigestSubject() {
        return weeklyDigestSubject;
    }

    public Pair<String, String> emailConfig(String frequency) {
        if (frequency.equalsIgnoreCase(MailConstants.DIGEST_DAILY)) {
            return Pair.of(dailyDigestSubject, dailyDigestEmail);
        }
        if (frequency.equalsIgnoreCase(MailConstants.DIGEST_WEEKLY)) {
            return Pair.of(weeklyDigestSubject, weeklyDigestEmail);
        }
        return null;
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
