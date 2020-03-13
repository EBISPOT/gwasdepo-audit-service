package uk.ac.ebi.spot.gwas.deposition.audit.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.AuditActionType;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.AuditMetadata;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.AuditObjectType;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditTrail;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.AuditEntryRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.AuditTrailRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.service.AuditEntriesService;
import uk.ac.ebi.spot.gwas.deposition.constants.SubmissionProvenanceType;
import uk.ac.ebi.spot.gwas.deposition.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuditEntriesServiceImpl implements AuditEntriesService {

    private static final Logger log = LoggerFactory.getLogger(AuditEntriesService.class);

    @Autowired
    private AuditEntryRepository auditEntryRepository;

    @Autowired
    private AuditTrailRepository auditTrailRepository;

    @Override
    public AuditEntry getEntry(String auditEntryId) {
        log.info("Retrieving audit entry: {}", auditEntryId);

        Optional<AuditEntry> auditEntryOptional = auditEntryRepository.findById(auditEntryId);
        if (!auditEntryOptional.isPresent()) {
            log.error("Unable to find audit entry: {}", auditEntryId);
            throw new EntityNotFoundException("Unable to find audit entry: " + auditEntryId);
        }
        log.info("Audit entry successfully retrieved: {}", auditEntryOptional.get().getId());
        return auditEntryOptional.get();
    }

    @Override
    @Async
    public void createEntry(AuditEntry auditEntry) {
        log.info("Creating audit entry: {} | {}", auditEntry.getEntityId(), auditEntry.getUserId());
        auditEntry = auditEntryRepository.insert(auditEntry);
        List<String> entries = new ArrayList<>();
        entries.add(auditEntry.getId());

        /**
         * TODO: userId can sometimes be null
         * retrieve activity in the last 24h and generate email if there is anything notable - gwas-dev-logs@ebi.ac.uk
         */

        if (auditEntry.getAction().equalsIgnoreCase(AuditActionType.CREATE.name())) {
            if (auditEntry.getEntityType().equalsIgnoreCase(AuditObjectType.SUBMISSION.name())) {
                AuditTrail auditTrail = new AuditTrail(auditEntry.getEntityId(), auditEntry.getEntityType());
                auditTrail.setAuditEntries(entries);
                auditTrailRepository.insert(auditTrail);

                if (auditEntry.getMetadata().get(AuditMetadata.PROVENANCE_TYPE.name()).equalsIgnoreCase(SubmissionProvenanceType.PUBLICATION.name())) {
                    AuditTrail pubAuditTrail = new AuditTrail(auditEntry.getContext(), AuditObjectType.PUBLICATION.name());
                    pubAuditTrail.setAuditEntries(entries);
                    auditTrailRepository.insert(pubAuditTrail);
                }
                return;
            }

            if (auditEntry.getEntityType().equalsIgnoreCase(AuditObjectType.MANUSCRIPT.name())) {
                AuditTrail auditTrail = new AuditTrail(auditEntry.getEntityId(), auditEntry.getEntityType());
                auditTrail.setAuditEntries(entries);
                auditTrailRepository.insert(auditTrail);
                return;
            }
        }


    }
}
