package uk.ac.ebi.spot.gwas.deposition.audit.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.ebi.spot.gwas.deposition.audit.AuditEntryDto;
import uk.ac.ebi.spot.gwas.deposition.audit.constants.AuditServiceConstants;
import uk.ac.ebi.spot.gwas.deposition.audit.domain.AuditEntry;
import uk.ac.ebi.spot.gwas.deposition.audit.repository.AuditEntryRepository;
import uk.ac.ebi.spot.gwas.deposition.constants.GeneralCommon;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {IntegrationTest.MockTaskExecutorConfig.class})
public class AuditEntriesControllerTest extends IntegrationTest {

    @Autowired
    private AuditEntryRepository auditEntryRepository;

    /**
     * GET /v1/audit-entries/{auditEntryId}
     */
    @Test
    public void shouldGetManuscript() throws Exception {
        AuditEntry auditEntry = createAuditEntry();
        String endpoint = GeneralCommon.API_V1 + AuditServiceConstants.API_AUDIT_ENTRIES + "/" + auditEntry.getId();

        String response = mockMvc.perform(get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AuditEntryDto actual = mapper.readValue(response, new TypeReference<AuditEntryDto>() {
        });
        assertEquals(auditEntry.getId(), actual.getId());
        assertEquals(auditEntry.getAction(), actual.getAction());
        assertEquals(auditEntry.getContext(), actual.getContext());
        assertEquals(auditEntry.getEntityId(), actual.getEntityId());
        assertEquals(auditEntry.getUserId(), actual.getUserId());
        assertEquals(auditEntry.getTimestamp(), actual.getTimestamp());
    }

    /**
     * POST /v1/audit-entries
     */
    @Test
    public void shouldCreateAuditEntry() throws Exception {
        createAuditEntry();
    }

    private AuditEntry createAuditEntry() throws Exception {
        String endpoint = GeneralCommon.API_V1 + AuditServiceConstants.API_AUDIT_ENTRIES;
        AuditEntryDto auditEntryDto = new AuditEntryDto(
                null,
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10),
                new HashMap<>(),
                DateTime.now());

        mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(auditEntryDto)))
                .andExpect(status().isCreated());
        assertEquals(1, auditEntryRepository.findAll().size());
        AuditEntry actual = auditEntryRepository.findAll().get(0);
        assertEquals(auditEntryDto.getAction(), actual.getAction());
        assertEquals(auditEntryDto.getContext(), actual.getContext());
        assertEquals(auditEntryDto.getEntityId(), actual.getEntityId());
        assertEquals(auditEntryDto.getUserId(), actual.getUserId());
        assertEquals(auditEntryDto.getTimestamp(), actual.getTimestamp());
        return actual;
    }

}
