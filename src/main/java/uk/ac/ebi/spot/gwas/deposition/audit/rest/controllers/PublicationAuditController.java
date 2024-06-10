package uk.ac.ebi.spot.gwas.deposition.audit.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.PagedResources;
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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = GeneralCommon.API_V1 + AuditServiceConstants.API_PUBLICATION)
public class PublicationAuditController {

    private static final Logger log = LoggerFactory.getLogger(PublicationAuditController.class);

    @Autowired
    PublicationAuditEntryService publicationAuditEntryService;

    @Autowired
    PublicationAuditEntryDtoAssembler publicationAuditEntryDtoAssembler;

    /**
     * GET /v1/publications/{publicationId}/publication-audit-entries/{auditEntryId}
     */
    @GetMapping(value = "/{publicationId}/publication-audit-entries",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public PagedResources<PublicationAuditEntryDto> getAuditEntries(PagedResourcesAssembler assembler,
                                                                    @RequestParam(value = AuditServiceConstants.PARAM_PUBID,
                                                                            required = false) String publicationId,
                                                                    @SortDefault(sort = "timestamp", direction = Sort.Direction.DESC)
                                                                    @PageableDefault(size = 10, page = 0) Pageable pageable) {
    Page<PublicationAuditEntry> publicationAuditEntries = publicationAuditEntryService.getPublicationAuditEntries(publicationId, pageable);
    return assembler.toResource(publicationAuditEntries, publicationAuditEntryDtoAssembler,
                linkTo(methodOn(PublicationAuditController.class).getAuditEntries(assembler, publicationId, pageable)).withSelfRel());

    }


    /**
     * GET /v1/publications/{publicationId}/publication-audit-entries/{auditEntryId}
     */
    @GetMapping(value = "/{publicationId}/publication-audit-entries/{auditEntryId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Resource<PublicationAuditEntryDto> getAuditEntry(@PathVariable String publicationId, @PathVariable String auditEntryId) {
        log.info("Request to get audit entry: {}", auditEntryId);
        PublicationAuditEntry publicationAuditEntry = publicationAuditEntryService.getAuditEntry(auditEntryId);
        log.info("Returning entry: {}", publicationAuditEntry.getId());
        return publicationAuditEntryDtoAssembler.toResource(publicationAuditEntry);
    }

    /**
     * POST /v1/publications/{publicationId}/publication-audit-entries
     */

    @PostMapping(value = "/{publicationId}/publication-audit-entries",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED  )
    public Resource<PublicationAuditEntryDto> createAuditEntry(@PathVariable String publicationId, @RequestBody PublicationAuditEntryDto publicationAuditEntryDto) {
        PublicationAuditEntry publicationAuditEntry = publicationAuditEntryDtoAssembler.disassemble(publicationAuditEntryDto);
        return publicationAuditEntryDtoAssembler.toResource(publicationAuditEntryService.createPublicationAuditEntry(publicationAuditEntry));
    }

}
