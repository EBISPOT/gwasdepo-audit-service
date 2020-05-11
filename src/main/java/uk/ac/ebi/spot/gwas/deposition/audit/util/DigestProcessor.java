package uk.ac.ebi.spot.gwas.deposition.audit.util;

import org.joda.time.DateTime;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.AuditActionType;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.AuditMetadata;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.AuditObjectType;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.AuditOperationOutcome;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.DigestEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.BodyOfWorkRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.PublicationRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.SubmissionRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.UserRepository;
import uk.ac.ebi.spot.gwas.deposition.constants.SubmissionProvenanceType;
import uk.ac.ebi.spot.gwas.deposition.domain.BodyOfWork;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DigestProcessor {

    private UserRepository userRepository;

    private SubmissionRepository submissionRepository;

    private PublicationRepository publicationRepository;

    private BodyOfWorkRepository bodyOfWorkRepository;

    private DigestEntry digestEntry;

    private int noSubmissions;

    private int noValidSubmissions;

    private int noFailedSubmissions;

    private List<EmailSubmissionObject> submissions;

    private List<EmailSubmissionObject> validSubmissions;

    private List<EmailSubmissionObject> failedSubmissions;

    public DigestProcessor(List<AuditEntry> auditEntryList,
                           UserRepository userRepository,
                           SubmissionRepository submissionRepository,
                           PublicationRepository publicationRepository,
                           BodyOfWorkRepository bodyOfWorkRepository) {
        this.userRepository = userRepository;
        this.submissionRepository = submissionRepository;
        this.publicationRepository = publicationRepository;
        this.bodyOfWorkRepository = bodyOfWorkRepository;
        this.submissions = new ArrayList<>();
        this.validSubmissions = new ArrayList<>();
        this.failedSubmissions = new ArrayList<>();
        this.noSubmissions = 0;
        this.noValidSubmissions = 0;
        this.noFailedSubmissions = 0;

        digestEntry = new DigestEntry();
        digestEntry.setTimestamp(DateTime.now());

        for (AuditEntry auditEntry : auditEntryList) {
            processEntry(auditEntry);
        }

        digestEntry.setNoSubmissions(noSubmissions);
        digestEntry.setNoValidSubmissions(noValidSubmissions);
        digestEntry.setNoFailedSubmissions(noFailedSubmissions);
        digestEntry.setSubmissions(submissions);
        digestEntry.setValidSubmissions(validSubmissions);
        digestEntry.setFailedSubmissions(failedSubmissions);
    }

    private void processEntry(AuditEntry auditEntry) {
        if (auditEntry.getAction().equalsIgnoreCase(AuditActionType.CREATE.name()) &&
                auditEntry.getEntityType().equalsIgnoreCase(AuditObjectType.SUBMISSION.name())) {
            Optional<User> userOptional = userRepository.findById(auditEntry.getUserId());
            if (userOptional.isPresent()) {
                String authorName = userOptional.get().getName();
                String contextId = auditEntry.getContext();
                String title = "N/A";
                if (auditEntry.getMetadata().get(AuditMetadata.PROVENANCE_TYPE.name()).equalsIgnoreCase(SubmissionProvenanceType.PUBLICATION.name())) {
                    authorName = auditEntry.getMetadata().get(AuditMetadata.AUTHOR.name());
                }
                if (auditEntry.getMetadata().containsKey(AuditMetadata.TITLE.name())) {
                    title = auditEntry.getMetadata().get(AuditMetadata.TITLE.name());
                }
                this.submissions.add(new EmailSubmissionObject(
                        contextId,
                        title,
                        auditEntry.getMetadata().get(AuditMetadata.PROVENANCE_TYPE.name()),
                        authorName,
                        userOptional.get().getName(),
                        null));
                this.noSubmissions++;
            }
        }

        if (auditEntry.getAction().equalsIgnoreCase(AuditActionType.VALIDATION.name()) &&
                auditEntry.getEntityType().equalsIgnoreCase(AuditObjectType.SUBMISSION.name())) {
            Optional<User> userOptional = userRepository.findById(auditEntry.getUserId());
            if (userOptional.isPresent()) {
                String authorName = userOptional.get().getName();
                String contextId = auditEntry.getContext();
                String title = getTitle(contextId, auditEntry.getMetadata().get(AuditMetadata.PROVENANCE_TYPE.name()));
                if (auditEntry.getMetadata().get(AuditMetadata.PROVENANCE_TYPE.name()).equalsIgnoreCase(SubmissionProvenanceType.PUBLICATION.name())) {
                    authorName = auditEntry.getMetadata().get(AuditMetadata.AUTHOR.name());
                }

                if (auditEntry.getOutcome().equalsIgnoreCase(AuditOperationOutcome.SUCCESS.name())) {
                    this.validSubmissions.add(new EmailSubmissionObject(
                            contextId,
                            title,
                            auditEntry.getMetadata().get(AuditMetadata.PROVENANCE_TYPE.name()),
                            authorName,
                            userOptional.get().getName(), null));
                    this.noValidSubmissions++;
                } else {
                    String error = auditEntry.getMetadata().get(AuditMetadata.ERROR.name());
                    this.failedSubmissions.add(new EmailSubmissionObject(
                            contextId,
                            title,
                            auditEntry.getMetadata().get(AuditMetadata.PROVENANCE_TYPE.name()),
                            authorName,
                            userOptional.get().getName(),
                            error != null ? error : "UNKNOWN"));
                    this.noFailedSubmissions++;
                }
            }
        }
    }

    private String getTitle(String submissionId, String provenanceType) {
        String title = "N/A";
        Optional<Submission> submissionOptional = submissionRepository.findByIdAndArchived(submissionId, false);
        if (!submissionOptional.isPresent()) {
            return title;
        }
        Submission submission = submissionOptional.get();
        if (provenanceType.equalsIgnoreCase(SubmissionProvenanceType.PUBLICATION.name())) {
            if (submission.getProvenanceType().equalsIgnoreCase(SubmissionProvenanceType.PUBLICATION.name())) {
                Optional<Publication> publicationOptional = publicationRepository.findById(submission.getPublicationId());
                if (publicationOptional.isPresent()) {
                    title = publicationOptional.get().getTitle();
                }
            }
        }
        if (provenanceType.equalsIgnoreCase(SubmissionProvenanceType.BODY_OF_WORK.name())) {
            if (submission.getProvenanceType().equalsIgnoreCase(SubmissionProvenanceType.BODY_OF_WORK.name())) {
                if (!submission.getBodyOfWorks().isEmpty()) {
                    Optional<BodyOfWork> bodyOfWorkOptional = bodyOfWorkRepository.findByBowIdAndArchived(submission.getBodyOfWorks().get(0), false);
                    if (bodyOfWorkOptional.isPresent()) {
                        title = bodyOfWorkOptional.get().getTitle();
                    }
                }
            }
        }
        return title;
    }

    public DigestEntry getDigestEntry() {
        return digestEntry;
    }
}
