package uk.ac.ebi.spot.gwas.deposition.audit.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "auditTrails")
public class AuditTrail {

    @Id
    private String id;

    @Indexed
    private String entityId;

    @Indexed
    private String entityType;

    private List<String> auditEntries;

    public AuditTrail() {

    }

    public AuditTrail(String entityId, String entityType) {
        this.entityId = entityId;
        this.entityType = entityType;
        this.auditEntries = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public List<String> getAuditEntries() {
        return auditEntries;
    }

    public void setAuditEntries(List<String> auditEntries) {
        this.auditEntries = auditEntries;
    }
}
