package xdcc;

import fr.xdcc.api.tasker.scheduler.BotCheckerJob;
import fr.xdcc.api.tasker.scheduler.FileCheckerJob;
import fr.xdcc.api.web.fluent.Server;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import xdcc.web.XdccApplication;

import java.util.Optional;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Main {

  public static void main(String[] args) throws Exception {
    new Server(new XdccApplication()).start(port());

    // TODO Refactor
    JobDetail botJob = newJob(BotCheckerJob.class)
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
    }
  }

  private static int port() {
    final Optional<String> port = Optional.ofNullable(System.getenv("PORT"));
    return Integer.parseInt(port.orElse("8080"));
  }
}
