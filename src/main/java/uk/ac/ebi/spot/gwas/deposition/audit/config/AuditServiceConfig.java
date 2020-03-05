package uk.ac.ebi.spot.gwas.deposition.audit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuditServiceConfig {

    @Value("${gwas-audit-service.auth.enabled}")
    private boolean authEnabled;

    @Value("${gwas-audit-service.db:#{NULL}}")
    private String dbName;

    public boolean isAuthEnabled() {
        return authEnabled;
    }

    public String getDbName() {
        return dbName;
    }

}
