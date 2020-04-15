package uk.ac.ebi.spot.gwas.deposition.audit.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.spot.gwas.deposition.audit.util.EmailSubmissionObject;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "digestEntries")
public class DigestEntry {

    @Id
    private String id;

    private DateTime timestamp;

    private int noSubmissions;

    private int noValidSubmissions;

    private int noFailedSubmissions;

    private List<EmailSubmissionObject> submissions;

    private List<EmailSubmissionObject> validSubmissions;

    private List<EmailSubmissionObject> failedSubmissions;

    public DigestEntry() {
        this.submissions = new ArrayList<>();
        this.validSubmissions = new ArrayList<>();
        this.failedSubmissions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getNoSubmissions() {
        return noSubmissions;
    }

    public void setNoSubmissions(int noSubmissions) {
        this.noSubmissions = noSubmissions;
    }

    public int getNoValidSubmissions() {
        return noValidSubmissions;
    }

    public void setNoValidSubmissions(int noValidSubmissions) {
        this.noValidSubmissions = noValidSubmissions;
    }

    public int getNoFailedSubmissions() {
        return noFailedSubmissions;
    }

    public void setNoFailedSubmissions(int noFailedSubmissions) {
        this.noFailedSubmissions = noFailedSubmissions;
    }

    public List<EmailSubmissionObject> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<EmailSubmissionObject> submissions) {
        this.submissions = submissions;
    }

    public List<EmailSubmissionObject> getValidSubmissions() {
        return validSubmissions;
    }

    public void setValidSubmissions(List<EmailSubmissionObject> validSubmissions) {
        this.validSubmissions = validSubmissions;
    }

    public List<EmailSubmissionObject> getFailedSubmissions() {
        return failedSubmissions;
    }

    public void setFailedSubmissions(List<EmailSubmissionObject> failedSubmissions) {
        this.failedSubmissions = failedSubmissions;
    }
}
