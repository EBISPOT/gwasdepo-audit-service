package uk.ac.ebi.spot.gwas.deposition.audit.scheduler.config;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.spot.gwas.deposition.audit.scheduler.jobs.WeeklyStatsJob;
import uk.ac.ebi.spot.gwas.deposition.scheduler.config.AbstractQuartzConfig;

import java.util.Date;

@Configuration
public class WeeklyStatsConfig extends AbstractQuartzConfig {

    private static final String JK_WAUDIT_STATS = "JK_WAUDIT_STATS";

    private static final String PG_WAUDIT_STATS = "PG_WAUDIT_STATS";

    private static final String TK_WAUDIT_STATS = "TK_WAUDIT_STATS";

    @Value("${quartz.jobs.weekly-stats.schedule}")
    private String weeklyStatsSchedule;

    public WeeklyStatsConfig() {
        super(TK_WAUDIT_STATS, PG_WAUDIT_STATS);
    }

    public JobDetail getjobDetail() {
        JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setKey(new JobKey(JK_WAUDIT_STATS, PG_WAUDIT_STATS));
        jobDetail.setJobClass(WeeklyStatsJob.class);
        jobDetail.setDurability(true);
        return jobDetail;
    }

    public Trigger createNewTrigger(Date startTime) {
        return TriggerBuilder.newTrigger()
                .forJob(this.getjobDetail())
                .withIdentity(TK_WAUDIT_STATS, PG_WAUDIT_STATS)
                .withPriority(50)
                .withSchedule(CronScheduleBuilder.cronSchedule(weeklyStatsSchedule))
                .startAt(startTime).build();
    }
}
