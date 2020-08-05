package uk.ac.ebi.spot.gwas.deposition.audit.util;

public class WeeklyEmailSubmissionObject {

    private String userId;

    private String provenanceType;

    private String objectId;

    private String pmid;

    private String firstAuthor;

    private String objectLink;

    private String gcpLink;

    private String state;

    private String date;

    public WeeklyEmailSubmissionObject(String userId, String objectId, String objectLink,
                                       String provenanceType, String gcpLink, String pmid,
                                       String firstAuthor, String state, String date) {
        this.userId = userId;
        this.provenanceType = provenanceType;
        this.objectId = objectId;
        this.pmid = pmid;
        this.firstAuthor = firstAuthor;
        this.objectLink = objectLink;
        this.gcpLink = gcpLink;
        this.state = state;
        this.date = date;
    }

    public String getPmid() {
        return pmid;
    }

    public String getFirstAuthor() {
        return firstAuthor;
    }

    public String getObjectLink() {
        return objectLink;
    }

    public String getGcpLink() {
        return gcpLink;
    }

    public String getUserId() {
        return userId;
    }

    public String getProvenanceType() {
        return provenanceType;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getState() {
        return state;
    }

    public String getDate() {
        return date;
    }
}
