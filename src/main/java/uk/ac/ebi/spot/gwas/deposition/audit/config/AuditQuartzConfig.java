package uk.ac.ebi.spot.gwas.deposition.audit.config;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.spot.gwas.deposition.audit.scheduler.config.FailedEmailConfig;
import uk.ac.ebi.spot.gwas.deposition.audit.scheduler.config.StatsConfig;
import uk.ac.ebi.spot.gwas.deposition.audit.scheduler.config.WeeklyStatsConfig;
import uk.ac.ebi.spot.gwas.deposition.scheduler.config.QuartzSchedulerJobConfig;

import javax.annotation.PostConstruct;

@Configuration
public class AuditQuartzConfig {

    @Autowired
    private QuartzSchedulerJobConfig quartzSchedulerJobConfig;

    @Autowired
    private StatsConfig statsConfig;

    @Autowired
    private WeeklyStatsConfig weeklyStatsConfig;

    @Autowired(required = false)
    private FailedEmailConfig failedEmailConfig;

    @PostConstruct
    private void initialize() throws SchedulerException {
        quartzSchedulerJobConfig.addJob(statsConfig);
        quartzSchedulerJobConfig.addJob(weeklyStatsConfig);
        if (failedEmailConfig != null) {
            quartzSchedulerJobConfig.addJob(failedEmailConfig);
        }
        quartzSchedulerJobConfig.initializeJobs();
    }
}
