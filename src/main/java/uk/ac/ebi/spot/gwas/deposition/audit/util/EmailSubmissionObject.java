package uk.ac.ebi.spot.gwas.deposition.audit.util;

public class EmailSubmissionObject {

    private String contextId;

    private String provenanceType;

    private String firstAuthor;

    private String userName;

    private String error;

    public EmailSubmissionObject(String contextId, String provenanceType, String firstAuthor, String userName, String error) {
        this.provenanceType = provenanceType;
        this.contextId = contextId;
        this.firstAuthor = firstAuthor;
        this.userName = userName;
        this.error = error;
    }

    public String getContextId() {
        return contextId;
    }

    public String getProvenanceType() {
        return provenanceType;
    }

    public String getFirstAuthor() {
        return firstAuthor;
    }

    public String getUserName() {
        return userName;
    }

    public String getError() {
        return error;
    }
}
