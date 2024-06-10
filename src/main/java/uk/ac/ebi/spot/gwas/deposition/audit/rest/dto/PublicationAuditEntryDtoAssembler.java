package uk.ac.ebi.spot.gwas.deposition.audit.rest.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;
import uk.ac.ebi.spot.gwas.deposition.audit.PublicationAuditEntryDto;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.PublicationAuditEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.rest.controllers.PublicationAuditEntryController;
import uk.ac.ebi.spot.gwas.deposition.audit.service.PublicationAuditEntryService;
import uk.ac.ebi.spot.gwas.deposition.audit.service.UserService;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
@Component
public class PublicationAuditEntryDtoAssembler extends ResourceSupport implements ResourceAssembler<PublicationAuditEntry, Resource<PublicationAuditEntryDto>> {

    @Autowired
    UserService userService;

    @Autowired
    UserDtoAssembler userDtoAssembler;

    @Autowired
    PublicationAuditEntryService publicationAuditEntryService;
    @Override
    public Resource<PublicationAuditEntryDto> toResource(PublicationAuditEntry auditEntry) {
        PublicationAuditEntryDto publicationAuditEntryDto = PublicationAuditEntryDto.builder()
                .publicationId(auditEntry.getPublicationId())
                .event(auditEntry.getEvent())
                .eventDetails(auditEntry.getEventDetails())
                .timestamp(auditEntry.getTimestamp())
                .userDto(userDtoAssembler.assemble(userService.getUserDetails(auditEntry.getUserId())))
                .build();

        Resource<PublicationAuditEntryDto> resource = new Resource<>(publicationAuditEntryDto);
        resource.add(linkTo(methodOn(PublicationAuditEntryController.class).getAuditEntry(auditEntry.getId())).withSelfRel());
        return resource;
    }


    public PublicationAuditEntry disassemble(PublicationAuditEntryDto publicationAuditEntryDto) {
        PublicationAuditEntry publicationAuditEntry = new PublicationAuditEntry();
        publicationAuditEntry.setPublicationId(publicationAuditEntryDto.getIsPublication() ? publicationAuditEntryDto.getPublicationId() : publicationAuditEntryService.getPublicationId(publicationAuditEntryDto));
        publicationAuditEntry.setEvent(publicationAuditEntryDto.getEvent());
        publicationAuditEntry.setEventDetails(publicationAuditEntryDto.getEventDetails());
        publicationAuditEntry.setTimestamp(publicationAuditEntryDto.getTimestamp());
        publicationAuditEntry.setUserId(publicationAuditEntryDto.getUserDto().getEmail());
        return publicationAuditEntry;
    }
}
