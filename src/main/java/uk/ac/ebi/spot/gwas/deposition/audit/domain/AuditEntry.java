package uk.ac.ebi.spot.gwas.deposition.audit.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "auditEntries")
@CompoundIndexes({@CompoundIndex(name = "archived_user", def = "{'created_userId': 1, 'archived': 1}"),
        @CompoundIndex(name = "id_archived", def = "{'id': 1, 'archived': 1}"),
        @CompoundIndex(name = "id_archived_user", def = "{'id': 1, 'archived': 1, 'created_userId': 1}")})
public class AuditEntry {

    @Id
    private String id;

    private String userId;

    private String action;

    private String entityId;

    private DateTime timestamp;

    private String context;

    public AuditEntry(String userId, String action, String entityId, DateTime timestamp, String context) {
        this.userId = userId;
        this.action = action;
        this.entityId = entityId;
        this.timestamp = timestamp;
        this.context = context;
    }

    public AuditEntry() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
