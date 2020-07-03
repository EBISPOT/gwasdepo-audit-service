package uk.ac.ebi.spot.gwas.deposition.audit.util;

public class WeeklyEmailAuthorObject {

    private String userId;

    private String userName;

    private String userEmail;

    public WeeklyEmailAuthorObject(String userId, String userName, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
