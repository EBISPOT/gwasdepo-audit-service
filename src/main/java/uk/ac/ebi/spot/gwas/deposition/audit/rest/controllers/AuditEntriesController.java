package uk.ac.ebi.spot.gwas.deposition.audit.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.AuditServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.rest.dto.AuditEntryDtoAssembler;
import uk.ac.ebi.spot.gwas.deposition.audit.service.AuditEntriesService;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.dto.AuditEntryDto;

@RestController
@RequestMapping(value = GeneralCommon.API_V1 + AuditServiceConstants.API_AUDIT_ENTRIES)
public class AuditEntriesController {

    private static final Logger log = LoggerFactory.getLogger(AuditEntriesController.class);

    @Autowired
    private AuditEntriesService auditEntriesService;

    /**
     * GET /v1/audit-entries/{auditEntryId}
     */
    @GetMapping(value = "/{auditEntryId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AuditEntryDto getAuditEntry(@PathVariable String auditEntryId) {
        log.info("Request to get audit entry: {}", auditEntryId);
        AuditEntry auditEntry = auditEntriesService.getEntry(auditEntryId);
        log.info("Returning entry: {}", auditEntry.getId());
        return AuditEntryDtoAssembler.assemble(auditEntry);
    }

    /**
     * POST /v1/audit-entries
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createAuditEntry(@RequestBody AuditEntryDto auditEntryDto) {
        log.info("Request to create audit entry: {}", auditEntryDto.getEntityId());
        AuditEntry auditEntry = AuditEntryDtoAssembler.disassenble(auditEntryDto);
        auditEntriesService.createEntry(auditEntry);
    }
}
