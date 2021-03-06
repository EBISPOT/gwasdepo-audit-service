package uk.ac.ebi.spot.gwas.deposition.audit.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "auditEntries")
@CompoundIndexes({@CompoundIndex(name = "context_action", def = "{'context': 1, 'action': 1}"),
        @CompoundIndex(name = "eId_time", def = "{'entityId': 1, 'timestamp': 1}"),
        @CompoundIndex(name = "context_time", def = "{'context': 1, 'timestamp': 1}")})
public class AuditEntry {

    @Id
    private String id;

    private String userId;

    private String action;

    private String outcome;

    private String entityId;

    private String entityType;

    private DateTime timestamp;

    private String context;

    private Map<String, String> metadata;

    public AuditEntry(String userId, String action, String outcome,
                      String entityId, String entityType, String context,
                      Map<String, String> metadata, DateTime timestamp) {
        this.userId = userId;
        this.action = action;
        this.outcome = outcome;
        this.entityId = entityId;
        this.entityType = entityType;
        this.timestamp = timestamp;
        this.metadata = metadata;
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

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}
