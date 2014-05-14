package fr.xdcc.api.tasker.scheduler;

import fr.xdcc.api.tasker.bot.SentryBot;
import fr.xdcc.api.tasker.service.TaskerService;
import org.jibble.pircbot.IrcException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

public class BotCheckerJob implements Job {

  @Inject
  public BotCheckerJob(TaskerService taskerService) {
    this.taskerService = taskerService;
  }

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    SentryBot sentryBot = new SentryBot(taskerService);
    // sentryBot.setVerbose(true);

    LOG.info("Starting job: BotCheckerJob");
    try {
      sentryBot.connect("irc.otaku-irc.fr");
      sentryBot.joinChannel("#serial_us");
    } catch (IrcException | IOException e) {
      LOG.debug("Failed to connect the bot to the server: {}", e.getMessage());
    }
    LOG.info("End of job: BotCheckerJob");
  }

  private final TaskerService taskerService;
  private static final Logger LOG = LoggerFactory.getLogger(BotCheckerJob.class);
}
