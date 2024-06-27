package uk.ac.ebi.spot.gwas.deposition.audit.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.audit.PublicationAuditEntryDto;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.PublicationAuditEntryRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.SubmissionRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.rest.controllers.PublicationAuditController;
import uk.ac.ebi.spot.gwas.deposition.audit.service.PublicationAuditEntryService;
import uk.ac.ebi.spot.gwas.deposition.domain.PublicationAuditEntry;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;

@Service
public class PublicationAuditEntryServiceImpl  implements PublicationAuditEntryService  {

    private static final Logger log = LoggerFactory.getLogger(PublicationAuditController.class);
    @Autowired
    PublicationAuditEntryRepository  publicationAuditEntryRepository;

    @Autowired
    SubmissionRepository submissionRepository;
    @Override
    public PublicationAuditEntry createPublicationAuditEntry(PublicationAuditEntry publicationAuditEntry) {
        return publicationAuditEntryRepository.save(publicationAuditEntry);
    }


    public PublicationAuditEntry getAuditEntry(String auditEntryId) {
      return   publicationAuditEntryRepository.findById(auditEntryId)
                .orElse(null);

    }


   public String getPublicationId(PublicationAuditEntryDto publicationAuditEntryDto) {
        if(!publicationAuditEntryDto.getPublication()){
        Submission submission = submissionRepository.findByIdAndArchived(publicationAuditEntryDto
                    .getPublicationId(), false).orElse(null);
            log.info("the PubId is {}",submission.getPublicationId());
        if(submission != null) {
            return submission.getPublicationId();
        }else {
            return null;
        }
        }
        return null;
   }
}


