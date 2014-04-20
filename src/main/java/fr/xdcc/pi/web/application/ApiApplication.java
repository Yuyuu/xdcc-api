package fr.xdcc.pi.web.application;

import fr.xdcc.pi.tasker.scheduler.BotCheckerJob;
import fr.xdcc.pi.tasker.scheduler.FileCheckerJob;
import fr.xdcc.pi.web.filter.LogRequestFilter;
import fr.xdcc.pi.web.resource.HomeResource;
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

    if (args.length < 1) {
      System.err.println("Usage:\nxdcc-pi <port>");
      System.exit(1);
    }

    int port = Integer.parseInt(args[0]);

    new WebServer(routes -> routes
        .add(HomeResource.class)
        .add(MongoBotResource.class)
        .filter(LogRequestFilter.class)
    ).start(port);

    /*JobDetail botJob = newJob(BotCheckerJob.class)
        .withIdentity("BotJob", "CheckerGroup").build();
    Trigger botTrigger = newTrigger()
        .withIdentity("TestTriggerBotJob", "CheckerGroup")
        .startAt(futureDate(10, DateBuilder.IntervalUnit.SECOND))
        .build();

    JobDetail fileJob = newJob(FileCheckerJob.class)
        .withIdentity("FileJob", "CheckerGroup")
        .usingJobData("max", 5)
        .usingJobData("offset", 0)
        .build();
    Trigger fileTrigger = newTrigger()
        .withIdentity("TestTriggerFileJob", "CheckerGroup")
        .startAt(futureDate(20, DateBuilder.IntervalUnit.MINUTE))
        .withSchedule(
            simpleSchedule()
                .withIntervalInMinutes(20)
                .repeatForever()
        )
        .build();

    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    Scheduler scheduler;
    try {
      scheduler = schedulerFactory.getScheduler();
      scheduler.scheduleJob(botJob, botTrigger);
      scheduler.scheduleJob(fileJob, fileTrigger);
      scheduler.start();
    } catch (SchedulerException e) {
      e.printStackTrace();
    }*/
  }
}
