package uk.ac.ebi.spot.gwas.deposition.audit.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.spot.gwas.deposition.audit.util.WeeklyEmailAuthorObject;
import uk.ac.ebi.spot.gwas.deposition.audit.util.WeeklyEmailSubmissionObject;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "weeklyDigestEntries")
public class WeeklyDigestEntry {

    @Id
    private String id;

    private DateTime timestamp;

    private int noSubmissions;

    private int noValidSubmissions;

    private int noFailedSubmissions;

    private List<WeeklyEmailSubmissionObject> submissions;

    private List<WeeklyEmailAuthorObject> users;

    public WeeklyDigestEntry() {
        this.submissions = new ArrayList<>();
        this.users = new ArrayList<>();
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

    public List<WeeklyEmailSubmissionObject> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<WeeklyEmailSubmissionObject> submissions) {
        this.submissions = submissions;
    }

    public List<WeeklyEmailAuthorObject> getUsers() {
        return users;
    }

    public void setUsers(List<WeeklyEmailAuthorObject> users) {
        this.users = users;
    }
}
