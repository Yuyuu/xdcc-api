package fr.xdcc.pi.tasker.scheduler;

import fr.xdcc.pi.tasker.bot.SentryBot;
import org.jibble.pircbot.IrcException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BotCheckerJob implements Job {

  private static final Logger LOG = LoggerFactory.getLogger(BotCheckerJob.class);

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    LOG.info("Starting job: BotCheckerJob");
    SentryBot sentryBot = new SentryBot("pi-sentry");
    // sentryBot.setVerbose(true);

    try {
      sentryBot.connect("irc.otaku-irc.fr");
      sentryBot.joinChannel("#serial_us");
    } catch (IrcException | IOException e) {
      LOG.debug("Failed to connect the bot to the server: {}", e.getMessage());
    }
    LOG.info("End of job: BotCheckerJob");
  }
}
