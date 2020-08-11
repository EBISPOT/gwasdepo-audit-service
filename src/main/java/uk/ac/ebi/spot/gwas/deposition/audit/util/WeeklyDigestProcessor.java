package uk.ac.ebi.spot.gwas.deposition.audit.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import uk.ac.ebi.spot.gwas.deposition.audit.config.AuditEmailConfig;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.AuditActionType;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.AuditMetadata;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.AuditObjectType;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.AuditOperationOutcome;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.WeeklyDigestEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.BodyOfWorkRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.PublicationRepository;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.UserRepository;
import uk.ac.ebi.spot.gwas.deposition.constants.SubmissionProvenanceType;
import uk.ac.ebi.spot.gwas.deposition.domain.Author;
import uk.ac.ebi.spot.gwas.deposition.domain.BodyOfWork;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.domain.User;

import java.util.*;

public class WeeklyDigestProcessor {

    private UserRepository userRepository;

    private PublicationRepository publicationRepository;

    private BodyOfWorkRepository bodyOfWorkRepository;

    private AuditEmailConfig auditEmailConfig;

    private WeeklyDigestEntry weeklyDigestEntry;

    private int noSubmissions;

    private int noValidSubmissions;

    private int noFailedSubmissions;

    private Map<String, List<WeeklyEmailSubmissionObject>> submissions;

    private Map<String, WeeklyEmailAuthorObject> users;

    public WeeklyDigestProcessor(List<AuditEntry> auditEntryList,
                                 UserRepository userRepository,
                                 PublicationRepository publicationRepository,
                                 BodyOfWorkRepository bodyOfWorkRepository,
                                 AuditEmailConfig auditEmailConfig) {
        this.userRepository = userRepository;
        this.publicationRepository = publicationRepository;
        this.bodyOfWorkRepository = bodyOfWorkRepository;
        this.auditEmailConfig = auditEmailConfig;
        this.submissions = new HashMap<>();
        this.users = new HashMap<>();
        this.noSubmissions = 0;
        this.noValidSubmissions = 0;
        this.noFailedSubmissions = 0;

        weeklyDigestEntry = new WeeklyDigestEntry();
        weeklyDigestEntry.setTimestamp(DateTime.now());

        for (AuditEntry auditEntry : auditEntryList) {
            processEntry(auditEntry);
        }

        weeklyDigestEntry.setNoSubmissions(noSubmissions);
        weeklyDigestEntry.setNoValidSubmissions(noValidSubmissions);
        weeklyDigestEntry.setNoFailedSubmissions(noFailedSubmissions);
        List<WeeklyEmailSubmissionObject> consolidated = new ArrayList<>();
        for (List<WeeklyEmailSubmissionObject> list : submissions.values()) {
            consolidated.addAll(list);
        }
        weeklyDigestEntry.setSubmissions(consolidated);
        List<WeeklyEmailAuthorObject> userList = new ArrayList<>();
        for (WeeklyEmailAuthorObject weeklyEmailAuthorObject : users.values()) {
            userList.add(weeklyEmailAuthorObject);
        }
        weeklyDigestEntry.setUsers(userList);
    }

    private void processEntry(AuditEntry auditEntry) {
        if (auditEntry.getAction().equalsIgnoreCase(AuditActionType.CREATE.name()) &&
                auditEntry.getEntityType().equalsIgnoreCase(AuditObjectType.SUBMISSION.name())) {
            Optional<User> userOptional = userRepository.findById(auditEntry.getUserId());
            if (userOptional.isPresent()) {
                String objectLink = auditEmailConfig.getBaseURL() + auditEmailConfig.getPrefixSubmission() + auditEntry.getEntityId();
                String gcpLink = null;
                String pmid = auditEntry.getContext();
                String firstAuthor = "N/A";

                if (auditEntry.getMetadata().get(AuditMetadata.PROVENANCE_TYPE.name()).equalsIgnoreCase(SubmissionProvenanceType.PUBLICATION.name())) {
                    Optional<Publication> publicationOptional = publicationRepository.findById(pmid);
                    if (publicationOptional.isPresent()) {
                        firstAuthor = publicationOptional.get().getFirstAuthor() != null ? publicationOptional.get().getFirstAuthor() : firstAuthor;
                    }
                } else {
                    gcpLink = auditEmailConfig.getBaseURL() + auditEmailConfig.getPrefixBodyOfWork() + auditEntry.getContext();
                    Optional<BodyOfWork> bowOptional = bodyOfWorkRepository.findByBowIdAndArchived(pmid, false);
                    if (bowOptional.isPresent()) {
                        firstAuthor = bowOptional.get().getFirstAuthor() != null ? getName(bowOptional.get().getFirstAuthor()) : firstAuthor;
                    }
                }

                User user = userOptional.get();
                if (!users.containsKey(user.getId())) {
                    users.put(user.getId(), new WeeklyEmailAuthorObject(user.getId(), user.getName(), user.getEmail()));
                }

                DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy");
                List<WeeklyEmailSubmissionObject> list = submissions.containsKey(user.getId()) ? submissions.get(user.getId()) : new ArrayList<>();
                list.add(new WeeklyEmailSubmissionObject(user.getId(),
                        auditEntry.getEntityId(),
                        objectLink,
                        DigestUtil.labelForProvenanceType(auditEntry.getMetadata().get(AuditMetadata.PROVENANCE_TYPE.name())),
                        gcpLink,
                        pmid,
                        firstAuthor,
                        "Created",
                        dtf.print(auditEntry.getTimestamp())
                ));
                submissions.put(user.getId(), list);
                this.noSubmissions++;
            }
        }

        if (auditEntry.getAction().equalsIgnoreCase(AuditActionType.VALIDATION.name()) &&
                auditEntry.getEntityType().equalsIgnoreCase(AuditObjectType.SUBMISSION.name())) {
            Optional<User> userOptional = userRepository.findById(auditEntry.getUserId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String pmid = auditEntry.getContext();
                String objectLink = auditEmailConfig.getBaseURL() + auditEmailConfig.getPrefixSubmission() + auditEntry.getEntityId();
                String gcpLink = null;
                String firstAuthor = "N/A";

                if (auditEntry.getMetadata().get(AuditMetadata.PROVENANCE_TYPE.name()).equalsIgnoreCase(SubmissionProvenanceType.PUBLICATION.name())) {
                    Optional<Publication> publicationOptional = publicationRepository.findById(pmid);
                    if (publicationOptional.isPresent()) {
                        pmid = publicationOptional.get().getPmid();
                        firstAuthor = publicationOptional.get().getFirstAuthor() != null ? publicationOptional.get().getFirstAuthor() : firstAuthor;
                    }
                } else {
                    gcpLink = auditEmailConfig.getBaseURL() + auditEmailConfig.getPrefixBodyOfWork() + auditEntry.getContext();
                    Optional<BodyOfWork> bowOptional = bodyOfWorkRepository.findByBowIdAndArchived(pmid, false);
                    if (bowOptional.isPresent()) {
                        firstAuthor = bowOptional.get().getFirstAuthor() != null ? getName(bowOptional.get().getFirstAuthor()) : firstAuthor;
                    }
                }

                if (!users.containsKey(user.getId())) {
                    users.put(user.getId(), new WeeklyEmailAuthorObject(user.getId(), user.getName(), user.getEmail()));
                }
                DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy");
                List<WeeklyEmailSubmissionObject> list = submissions.containsKey(user.getId()) ? submissions.get(user.getId()) : new ArrayList<>();
                if (auditEntry.getOutcome().equalsIgnoreCase(AuditOperationOutcome.SUCCESS.name())) {
                    list.add(new WeeklyEmailSubmissionObject(user.getId(),
                            auditEntry.getEntityId(),
                            objectLink,
                            DigestUtil.labelForProvenanceType(auditEntry.getMetadata().get(AuditMetadata.PROVENANCE_TYPE.name())),
                            gcpLink,
                            pmid,
                            firstAuthor,
                            "Successful",
                            dtf.print(auditEntry.getTimestamp())
                    ));
                    this.noValidSubmissions++;
                } else {
                    list.add(new WeeklyEmailSubmissionObject(user.getId(),
                            auditEntry.getEntityId(),
                            objectLink,
                            DigestUtil.labelForProvenanceType(auditEntry.getMetadata().get(AuditMetadata.PROVENANCE_TYPE.name())),
                            gcpLink,
                            pmid,
                            firstAuthor,
                            "Failed",
                            dtf.print(auditEntry.getTimestamp())
                    ));
                    this.noFailedSubmissions++;
                }

                submissions.put(user.getId(), list);
            }
        }
    }

    private String getName(Author firstAuthor) {
        String name = "";
        if (firstAuthor.getFirstName() != null) {
            name += firstAuthor.getFirstName();
        }
        name = name.trim();
        if (firstAuthor.getLastName() != null) {
            if (!name.equalsIgnoreCase("")) {
                name += " ";
            }
            name += firstAuthor.getLastName();
        }
        name = name.trim();
        if (name.equalsIgnoreCase("")) {
            if (firstAuthor.getGroup() != null) {
                name = firstAuthor.getGroup();
            }
        }
        name = name.trim();
        return name.equalsIgnoreCase("") ? "N/A" : name;
    }

    public WeeklyDigestEntry getWeeklyDigestEntry() {
        return weeklyDigestEntry;
    }
}
