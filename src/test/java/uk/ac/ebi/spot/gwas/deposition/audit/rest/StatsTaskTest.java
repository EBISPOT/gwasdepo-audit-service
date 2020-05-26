package uk.ac.ebi.spot.gwas.deposition.audit.rest;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.ebi.spot.gwas.deposition.audit.config.AuditEmailConfig;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.*;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.DigestEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.AuditEntryRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.DigestEntryRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.UserRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.scheduler.tasks.StatsTask;
import uk.ac.ebi.spot.gwas.deposition.audit.util.StatsEmailBuilder;
import uk.ac.ebi.spot.gwas.deposition.constants.SubmissionProvenanceType;
import uk.ac.ebi.spot.gwas.deposition.domain.User;
import uk.ac.ebi.spot.gwas.deposition.messaging.email.EmailBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(classes = {IntegrationTest.MockTaskExecutorConfig.class})
public class StatsTaskTest extends IntegrationTest {

    @Autowired
    private AuditEntryRepository auditEntryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatsTask statsTask;

    @Autowired
    private DigestEntryRepository digestEntryRepository;

    @Autowired
    private AuditEmailConfig auditEmailConfig;

    private User user;

    private String subPubId;

    private String subBoWId;

    private String pmidId;

    private String bowId;

    private String error;

    private DateTime dateTime;

    public void setup() {
        super.setup();

        user = new User("user name", "user email");
        user = userRepository.insert(user);

        subPubId = RandomStringUtils.randomAlphanumeric(10);
        subBoWId = RandomStringUtils.randomAlphanumeric(10);
        pmidId = RandomStringUtils.randomAlphanumeric(10);
        bowId = RandomStringUtils.randomAlphanumeric(10);
        error = RandomStringUtils.randomAlphanumeric(10);
        dateTime = DateTime.now().minusDays(1).plusHours(2);
    }

    @Test
    public void shouldGenerateStats() {
        createData();
        statsTask.buildStats();
        List<DigestEntry> digestEntryList = digestEntryRepository.findAll();
        assertEquals(1, digestEntryList.size());

        DigestEntry digestEntry = digestEntryList.get(0);
        assertEquals(2, digestEntry.getNoSubmissions());
        assertEquals(1, digestEntry.getNoValidSubmissions());
        assertEquals(1, digestEntry.getNoFailedSubmissions());

        assertEquals(2, digestEntry.getSubmissions().size());
        assertEquals(1, digestEntry.getValidSubmissions().size());
        assertEquals(1, digestEntry.getFailedSubmissions().size());

        assertEquals(user.getName(), digestEntry.getValidSubmissions().get(0).getUserName());
        assertEquals(pmidId, digestEntry.getValidSubmissions().get(0).getContextId());
        assertNotNull(digestEntry.getValidSubmissions().get(0).getFirstAuthor());
        assertEquals(SubmissionProvenanceType.PUBLICATION.name(), digestEntry.getValidSubmissions().get(0).getProvenanceType());

        assertEquals(user.getName(), digestEntry.getFailedSubmissions().get(0).getUserName());
        assertEquals(bowId, digestEntry.getFailedSubmissions().get(0).getContextId());
        assertEquals(error, digestEntry.getFailedSubmissions().get(0).getError());
        assertEquals(SubmissionProvenanceType.BODY_OF_WORK.name(), digestEntry.getFailedSubmissions().get(0).getProvenanceType());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(MailConstants.SUBMISSIONS_CREATED, digestEntry.getNoSubmissions());
        metadata.put(MailConstants.VALIDATION_SUCCESSFUL, digestEntry.getNoValidSubmissions());
        metadata.put(MailConstants.VALIDATION_FAILED, digestEntry.getNoFailedSubmissions());
        metadata.put(MailConstants.SUBMISSIONS, digestEntry.getSubmissions());
        metadata.put(MailConstants.VALID_SUBMISSIONS, digestEntry.getValidSubmissions());
        metadata.put(MailConstants.FAILED_SUBMISSIONS, digestEntry.getFailedSubmissions());

        EmailBuilder successBuilder = new StatsEmailBuilder(auditEmailConfig.getDailyDigestEmail());
        String content = successBuilder.getEmailContent(metadata);
        assertNotNull(content);
    }

    private void createData() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put(AuditMetadata.PROVENANCE_TYPE.name(), SubmissionProvenanceType.PUBLICATION.name());
        metadata.put(AuditMetadata.AUTHOR.name(), "Author name");
        auditEntryRepository.insert(new AuditEntry(user.getId(),
                AuditActionType.CREATE.name(),
                AuditOperationOutcome.SUCCESS.name(),
                subPubId,
                AuditObjectType.SUBMISSION.name(),
                pmidId,
                metadata,
                dateTime));

        metadata = new HashMap<>();
        metadata.put(AuditMetadata.PROVENANCE_TYPE.name(), SubmissionProvenanceType.BODY_OF_WORK.name());
        auditEntryRepository.insert(new AuditEntry(user.getId(),
                AuditActionType.CREATE.name(),
                AuditOperationOutcome.SUCCESS.name(),
                subBoWId,
                AuditObjectType.SUBMISSION.name(),
                bowId,
                metadata,
                dateTime));

        metadata = new HashMap<>();
        metadata.put(AuditMetadata.PROVENANCE_TYPE.name(), SubmissionProvenanceType.PUBLICATION.name());
        metadata.put(AuditMetadata.AUTHOR.name(), SubmissionProvenanceType.PUBLICATION.name());
        auditEntryRepository.insert(new AuditEntry(user.getId(),
                AuditActionType.VALIDATION.name(),
                AuditOperationOutcome.SUCCESS.name(),
                subPubId,
                AuditObjectType.SUBMISSION.name(),
                pmidId,
                metadata,
                dateTime));

        metadata = new HashMap<>();
        metadata.put(AuditMetadata.PROVENANCE_TYPE.name(), SubmissionProvenanceType.BODY_OF_WORK.name());
        metadata.put(AuditMetadata.ERROR.name(), error);
        auditEntryRepository.insert(new AuditEntry(user.getId(),
                AuditActionType.VALIDATION.name(),
                AuditOperationOutcome.FAILED.name(),
                subBoWId,
                AuditObjectType.SUBMISSION.name(),
                bowId,
                metadata,
                dateTime));
    }
}
