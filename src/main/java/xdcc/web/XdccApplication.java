package xdcc.web;

import fr.xdcc.api.tasker.scheduler.BotCheckerJob;
import fr.xdcc.api.tasker.scheduler.FileCheckerJob;
import fr.xdcc.api.web.fluent.BaseApplication;
import net.codestory.http.Configuration;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import xdcc.web.filter.LogRequestFilter;
import xdcc.web.resource.IndexResource;
import xdcc.web.resource.bot.MongoBotResource;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class XdccApplication extends BaseApplication {

  @Override
  protected Configuration routes() {
    return routes -> routes
        .filter(LogRequestFilter.class)
        .add(IndexResource.class)
        .add(MongoBotResource.class);
  }

  @Override
  protected void start() throws Exception {
    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    Scheduler scheduler = schedulerFactory.getScheduler();

    configureBotJob(scheduler);
    configureFileJob(scheduler);

    super.start();
    scheduler.start();
  }

  private void configureBotJob(Scheduler scheduler) throws SchedulerException {
    JobDetail botJob = newJob(BotCheckerJob.class)
        .withIdentity("BotJob", "CheckerGroup").build();
    Trigger botTrigger = newTrigger()
        .withIdentity("BotJobTrigger", "CheckerGroup")
        .startAt(futureDate(10, DateBuilder.IntervalUnit.SECOND))
        .build();

    scheduler.scheduleJob(botJob, botTrigger);
  }

  private void configureFileJob(Scheduler scheduler) throws SchedulerException {
    JobDetail fileJob = newJob(FileCheckerJob.class)
        .withIdentity("FileJob", "CheckerGroup")
        .usingJobData("max", 5)
        .usingJobData("offset", 0)
        .build();
    Trigger fileTrigger = newTrigger()
        .withIdentity("FileJobTrigger", "CheckerGroup")
        .startAt(futureDate(20, DateBuilder.IntervalUnit.MINUTE))
        .withSchedule(simpleSchedule().withIntervalInMinutes(20).repeatForever())
        .build();

    scheduler.scheduleJob(fileJob, fileTrigger);
  }
}
