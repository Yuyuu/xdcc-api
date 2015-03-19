package xdcc.web;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import fr.vter.xdcc.tasker.scheduler.BotCheckerJob;
import fr.vter.xdcc.tasker.scheduler.FileCheckerJob;
import fr.vter.xdcc.web.fluent.BaseApplication;
import net.codestory.http.Configuration;
import net.codestory.http.injection.GuiceAdapter;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import xdcc.web.configuration.GuiceConfiguration;
import xdcc.web.filter.AuthenticationFilter;
import xdcc.web.filter.LogRequestFilter;
import xdcc.web.resource.IndexResource;
import xdcc.web.resource.bot.MongoBotResource;
import xdcc.web.resource.login.AuthenticationResource;

import java.util.List;
import java.util.Optional;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class XdccApplication extends BaseApplication {

  public XdccApplication() {
    injector = Guice.createInjector(stage(), new GuiceConfiguration());
  }

  private Stage stage() {
    final Optional<String> env = Optional.ofNullable(System.getenv("env"));
    LOG.info("Configuration mode: {}", env.orElse("dev"));
    if (env.orElse("dev").equals("dev")) {
      return Stage.DEVELOPMENT;
    }
    return Stage.PRODUCTION;
  }

  @Override
  protected Configuration routes() {
    List<String> protectedRoutes = Lists.newLinkedList();

    return routes -> routes
        .setIocAdapter(new GuiceAdapter(injector))
        .filter(LogRequestFilter.class)
        .filter(new AuthenticationFilter(protectedRoutes, "xdcc:sessions"))
        .add(IndexResource.class)
        .add(AuthenticationResource.class)
        .add(MongoBotResource.class);
  }

  @Override
  protected void start() throws Exception {
    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    Scheduler scheduler = schedulerFactory.getScheduler();

    scheduler.setJobFactory(injector.getInstance(JobFactory.class));

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

  private final Injector injector;
}
