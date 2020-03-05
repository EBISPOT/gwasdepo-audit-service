package uk.ac.ebi.spot.gwas.deposition.audit.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.AuditEntryRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.service.AuditEntriesService;
import uk.ac.ebi.spot.gwas.deposition.exception.EntityNotFoundException;

import java.util.Optional;

@Service
public class AuditEntriesServiceImpl implements AuditEntriesService {

    private static final Logger log = LoggerFactory.getLogger(AuditEntriesService.class);

    @Autowired
    private AuditEntryRepository auditEntryRepository;

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
    public void createEntry(AuditEntry auditEntry) {
        log.info("Creating audit entry: {} | {}", auditEntry.getEntityId(), auditEntry.getUserId());
        auditEntryRepository.insert(auditEntry);
    }
}
