package uk.ac.ebi.spot.gwas.deposition.audit.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.spot.gwas.deposition.audit.PublicationAuditEntryDto;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.AuditServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.PublicationAuditEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.rest.dto.PublicationAuditEntryDtoAssembler;
import uk.ac.ebi.spot.gwas.deposition.audit.service.PublicationAuditEntryService;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;

@RestController
@RequestMapping(value = GeneralCommon.API_V1 + AuditServiceConstants.API_PUBLICATION_AUDIT_ENTRIES)
public class PublicationAuditEntryController {

    private static final Logger log = LoggerFactory.getLogger(PublicationAuditEntryController.class);

    @Autowired
    PublicationAuditEntryService publicationAuditEntryService;

    @Autowired
    PublicationAuditEntryDtoAssembler publicationAuditEntryDtoAssembler;

    /**
     * GET /v1/publication-audit-entries/{auditEntryId}
     */
    @GetMapping(value = "/{auditEntryId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Resource<PublicationAuditEntryDto> getAuditEntry(@PathVariable String auditEntryId) {
        log.info("Request to get audit entry: {}", auditEntryId);
        PublicationAuditEntry publicationAuditEntry = publicationAuditEntryService.getAuditEntry(auditEntryId);
        log.info("Returning entry: {}", publicationAuditEntry.getId());
        return publicationAuditEntryDtoAssembler.toResource(publicationAuditEntry);
    }




}
