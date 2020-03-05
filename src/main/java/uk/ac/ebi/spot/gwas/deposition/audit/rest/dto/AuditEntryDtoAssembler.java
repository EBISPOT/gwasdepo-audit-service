package uk.ac.ebi.spot.gwas.deposition.audit.rest.dto;

import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditEntry;
import uk.ac.ebi.spot.gwas.deposition.dto.AuditEntryDto;

public class AuditEntryDtoAssembler {

    public static AuditEntryDto assemble(AuditEntry auditEntry) {
        return new AuditEntryDto(auditEntry.getId(),
                auditEntry.getUserId(),
                auditEntry.getAction(),
                auditEntry.getEntityId(),
                auditEntry.getContext(),
                auditEntry.getTimestamp());
    }

    public static AuditEntry disassenble(AuditEntryDto auditEntryDto) {
        return new AuditEntry(auditEntryDto.getUserId(),
                auditEntryDto.getAction(),
                auditEntryDto.getEntityId(),
                auditEntryDto.getTimestamp(),
                auditEntryDto.getContext());
    }
}
