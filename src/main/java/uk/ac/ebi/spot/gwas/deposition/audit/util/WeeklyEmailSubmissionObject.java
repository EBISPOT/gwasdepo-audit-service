package uk.ac.ebi.spot.gwas.deposition.audit.util;

public class WeeklyEmailSubmissionObject {

    private String userId;

    private String provenanceType;

    private String objectId;

    private String state;

    private String date;

    public WeeklyEmailSubmissionObject(String userId, String objectId, String provenanceType, String state, String date) {
        this.userId = userId;
        this.provenanceType = provenanceType;
        this.objectId = objectId;
        this.state = state;
        this.date = date;
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
