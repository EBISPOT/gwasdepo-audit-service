package uk.ac.ebi.spot.gwas.deposition.audit.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;
import uk.ac.ebi.spot.gwas.deposition.messaging.email.AbstractEmailBuilder;
import uk.ac.ebi.spot.gwas.deposition.messaging.email.EmailBuilder;

import java.util.Map;

public class StatsEmailBuilder extends AbstractEmailBuilder implements EmailBuilder {

    private static final Logger log = LoggerFactory.getLogger(StatsEmailBuilder.class);

    public StatsEmailBuilder(String emailFile) {
        super(emailFile);
    }

    @Override
    public String getEmailContent(Map<String, Object> metadata) {
        log.info("Building stats email from: {}", emailFile);
        String content = super.readEmailContent();
        if (content != null) {
            Context context = new Context();
            for (String variable : metadata.keySet()) {
                Object variableValue = metadata.get(variable);
                context.setVariable(variable, variableValue);
            }
            return templateEngine.process(content, context);
        }
        return null;
    }
}
