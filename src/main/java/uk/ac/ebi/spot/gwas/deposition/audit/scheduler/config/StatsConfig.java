package uk.ac.ebi.spot.gwas.deposition.audit.scheduler.config;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.spot.gwas.deposition.scheduler.config.AbstractQuartzConfig;

import java.util.Date;

@Configuration
@ConditionalOnProperty(name = "gwas-deposition.stats-task.enabled", havingValue = "true")
public class StatsConfig extends AbstractQuartzConfig {

    private static final String JK_AUDIT_STATS = "JK_AUDIT_STATS";

    private static final String PG_AUDIT_STATS = "PG_AUDIT_STATS";

    private static final String TK_AUDIT_STATS = "TK_AUDIT_STATS";

    @Value("${quartz.jobs.daily-stats.schedule}")
    private String statsSchedule;

    public StatsConfig() {
        super(TK_AUDIT_STATS, PG_AUDIT_STATS);
    }

    public JobDetail getjobDetail() {
        JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setKey(new JobKey(JK_AUDIT_STATS, PG_AUDIT_STATS));
        jobDetail.setJobClass(StatsJob.class);
        jobDetail.setDurability(true);
        return jobDetail;
    }

    public Trigger createNewTrigger(Date startTime) {
        return TriggerBuilder.newTrigger()
                .forJob(this.getjobDetail())
                .withIdentity(TK_AUDIT_STATS, PG_AUDIT_STATS)
                .withPriority(50)
                .withSchedule(CronScheduleBuilder.cronSchedule(statsSchedule))
                .startAt(startTime).build();
    }
}
