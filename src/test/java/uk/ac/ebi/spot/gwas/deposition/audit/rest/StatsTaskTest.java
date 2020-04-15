package uk.ac.ebi.spot.gwas.deposition.audit.rest;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {IntegrationTest.MockTaskExecutorConfig.class})
public class StatsTaskTest extends IntegrationTest {

    public void setup() {
        super.setup();
    }

    @Test
    public void shouldGenerateStats() {

    }
}
