package uk.ac.ebi.spot.gwas.deposition.audit.util;

import java.util.ArrayList;
import java.util.List;

public class SubmissionDigestCheck {

    private List<EmailSubmissionObject> result;

    public SubmissionDigestCheck(List<EmailSubmissionObject> emailSubmissionObjects) {
        result = new ArrayList<>();
        if (emailSubmissionObjects != null) {
            for (EmailSubmissionObject emailSubmissionObject : emailSubmissionObjects) {
                EmailSubmissionObject checked = check(emailSubmissionObject);
                result.add(checked);
            }
        }
    }

    private EmailSubmissionObject check(EmailSubmissionObject emailSubmissionObject) {
        String provenanceType = "N/A";
        String title = "N/A";
        String contextId = "N/A";
        String firstAuthor = "N/A";
        String userName = "N/A";
        String embargo = "N/A";
        String error = "N/A";

        if (emailSubmissionObject.getContextId() != null) {
            contextId = emailSubmissionObject.getContextId();
        }
        if (emailSubmissionObject.getEmbargo() != null) {
            embargo = emailSubmissionObject.getEmbargo();
        }
        if (emailSubmissionObject.getError() != null) {
            error = emailSubmissionObject.getError();
        }
        if (emailSubmissionObject.getFirstAuthor() != null) {
            firstAuthor = emailSubmissionObject.getFirstAuthor();
        }
        if (emailSubmissionObject.getProvenanceType() != null) {
            provenanceType = emailSubmissionObject.getProvenanceType();
        }
        if (emailSubmissionObject.getTitle() != null) {
            title = emailSubmissionObject.getTitle();
        }
        if (emailSubmissionObject.getUserName() != null) {
            userName = emailSubmissionObject.getUserName();
        }
        return new EmailSubmissionObject(contextId, title, provenanceType,
                firstAuthor, userName, embargo, error);
    }

    public List<EmailSubmissionObject> getResult() {
        return result;
    }
}
