package uk.ac.ebi.spot.gwas.deposition.audit.rest.dto;

import uk.ac.ebi.spot.gwas.deposition.audit.AuditEntryDto;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditEntry;

public class AuditEntryDtoAssembler {

    public static AuditEntryDto assemble(AuditEntry auditEntry) {
        return new AuditEntryDto(auditEntry.getId(),
                auditEntry.getUserId(),
                auditEntry.getAction(),
                auditEntry.getOutcome(),
                auditEntry.getEntityId(),
                auditEntry.getEntityType(),
                auditEntry.getContext(),
                auditEntry.getMetadata(),
                auditEntry.getTimestamp());
    }

    public static AuditEntry disassenble(AuditEntryDto auditEntryDto) {
        return new AuditEntry(auditEntryDto.getUserId(),
                auditEntryDto.getAction(),
                auditEntryDto.getOutcome(),
                auditEntryDto.getEntityId(),
                auditEntryDto.getEntityType(),
                auditEntryDto.getContext(),
                auditEntryDto.getMetadata(),
                auditEntryDto.getTimestamp());
    }
}
