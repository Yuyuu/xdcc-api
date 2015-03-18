package fr.vter.xdcc.tasker.scheduler;

import com.google.inject.Injector;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.simpl.PropertySettingJobFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import javax.inject.Inject;

public class GuiceJobFactory extends PropertySettingJobFactory implements JobFactory {

  @Inject
  public GuiceJobFactory(Injector injector) {
    this.injector = injector;
  }

  @Override
  public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
    Job job =  injector.getInstance(bundle.getJobDetail().getJobClass());

    JobDataMap jobDataMap = new JobDataMap();
    jobDataMap.putAll(scheduler.getContext());
    jobDataMap.putAll(bundle.getJobDetail().getJobDataMap());
    jobDataMap.putAll(bundle.getTrigger().getJobDataMap());

    super.setBeanProps(job, jobDataMap);

    return job;
  }

  private final Injector injector;
}
