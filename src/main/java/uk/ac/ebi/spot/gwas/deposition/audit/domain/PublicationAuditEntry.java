package uk.ac.ebi.spot.gwas.deposition.audit.domain;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "publicationAuditEntries")
@CompoundIndexes({@CompoundIndex(name = "publication_timestamp",def = "{'publicationId': 1, 'timestamp': 1}")})
@Data
public class PublicationAuditEntry {

    @Id
    private String id;

    @Indexed
    private String publicationId;

    @Indexed
    private String event;

    private String eventDetails;

    private DateTime timestamp;

    private String userId;



}
