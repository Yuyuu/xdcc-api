package fr.xdcc.pi.web.application;

import fr.xdcc.pi.web.filter.LogRequestFilter;
import fr.xdcc.pi.web.resource.MongoBotResource;
import net.codestory.http.WebServer;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class ApiApplication {

  public static void main(String[] args) {
    new WebServer(routes -> routes
        .add(MongoBotResource.class)
        .filter(LogRequestFilter.class)
        .get("/", "xdcc-pi Web Server")
    ).start(8089);

    /*JobDetail job = newJob(BotCheckerJob.class)
        .withIdentity("BotJob", "CheckerGroup").build();
    Trigger trigger = newTrigger()
        .withIdentity("TestTrigger", "CheckerGroup")
        .startAt(futureDate(10, DateBuilder.IntervalUnit.SECOND))
        .build();

    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    Scheduler scheduler;
    try {
      scheduler = schedulerFactory.getScheduler();
      scheduler.scheduleJob(job, trigger);
      scheduler.start();
    } catch (SchedulerException e) {
      e.printStackTrace();
    }*/

    /*JobDetail job = newJob(FileCheckerJob.class)
        .withIdentity("FileJob", "CheckerGroup")
        .usingJobData("max", 5)
        .usingJobData("offset", 0)
        .build();
    Trigger trigger = newTrigger()
        .withIdentity("TestTrigger", "CheckerGroup")
        .startAt(futureDate(10, DateBuilder.IntervalUnit.SECOND))
        .withSchedule(
            simpleSchedule()
                .withIntervalInSeconds(30)
                .repeatForever()
        )
        .build();

    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    Scheduler scheduler;
    try {
      scheduler = schedulerFactory.getScheduler();
      scheduler.scheduleJob(job, trigger);
      scheduler.start();
    } catch (SchedulerException e) {
      e.printStackTrace();
    }*/
  }
}
