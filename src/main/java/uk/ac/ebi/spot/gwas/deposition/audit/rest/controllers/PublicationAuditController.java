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
import uk.ac.ebi.spot.gwas.deposition.audit.rest.dto.PublicationAuditEntryDtoAssembler;
import uk.ac.ebi.spot.gwas.deposition.audit.service.PublicationAuditEntryService;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;
import uk.ac.ebi.spot.gwas.deposition.domain.PublicationAuditEntry;

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
     * POST /v1/publications/{publicationId}/publication-audit-entries
     */

    @PostMapping(value = "/{publicationId}/publication-audit-entries",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED  )
    public Resource<PublicationAuditEntryDto> createAuditEntry(@PathVariable String publicationId, @RequestBody PublicationAuditEntryDto publicationAuditEntryDto) {
        log.info("Inside createAuditEntry()");
        log.info("The date in publicationAuditEntryDto is {}",publicationAuditEntryDto.getProvenanceDto().getTimestamp());
        log.info("The publication is submission or Pmid {}",publicationAuditEntryDto.getPublication());
        PublicationAuditEntry publicationAuditEntry = publicationAuditEntryDtoAssembler.disassemble(publicationAuditEntryDto);
        return publicationAuditEntryDtoAssembler.toResource(publicationAuditEntryService.createPublicationAuditEntry(publicationAuditEntry));
    }

}
