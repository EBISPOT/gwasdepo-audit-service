package uk.ac.ebi.spot.gwas.deposition.audit.scheduler.config;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.spot.gwas.deposition.audit.scheduler.jobs.FailedEmailJob;
import uk.ac.ebi.spot.gwas.deposition.scheduler.config.AbstractQuartzConfig;

import java.util.Date;

@Configuration
@ConditionalOnProperty(name = "email.enabled", havingValue = "true")
public class FailedEmailConfig extends AbstractQuartzConfig {

    private static final String JK_FAILED_EMAIL = "JK_FAILED_EMAIL";

    private static final String PG_FAILED_EMAIL = "PG_FAILED_EMAIL";

    private static final String TK_FAILED_EMAIL = "TK_FAILED_EMAIL";

    @Value("${quartz.jobs.failed-email.schedule}")
    private String failedEmailSchedule;

    public FailedEmailConfig() {
        super(TK_FAILED_EMAIL, PG_FAILED_EMAIL);
    }

    public JobDetail getjobDetail() {
        JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setKey(new JobKey(JK_FAILED_EMAIL, PG_FAILED_EMAIL));
        jobDetail.setJobClass(FailedEmailJob.class);
        jobDetail.setDurability(true);
        return jobDetail;
    }

    public Trigger createNewTrigger(Date startTime) {
        return TriggerBuilder.newTrigger()
                .forJob(this.getjobDetail())
                .withIdentity(TK_FAILED_EMAIL, PG_FAILED_EMAIL)
                .withPriority(50)
                .withSchedule(CronScheduleBuilder.cronSchedule(failedEmailSchedule))
                .startAt(startTime).build();
    }
}
