package uk.ac.ebi.spot.gwas.deposition.audit.util;

public class EmailSubmissionObject {

    private String contextId;

    private String provenanceType;

    private String firstAuthor;

    private String title;

    private String userName;

    private String embargo;

    private String error;

    public EmailSubmissionObject(String contextId, String title, String provenanceType, String firstAuthor, String userName, String embargo, String error) {
        this.provenanceType = provenanceType;
        this.title = title;
        this.contextId = contextId;
        this.firstAuthor = firstAuthor;
        this.userName = userName;
        this.embargo = embargo;
        this.error = error;
    }

    public String getEmbargo() {
        return embargo;
    }

    public String getTitle() {
        return title;
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
