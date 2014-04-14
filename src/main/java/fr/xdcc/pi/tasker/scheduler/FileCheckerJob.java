package fr.xdcc.pi.tasker.scheduler;

import fr.xdcc.pi.model.bot.MongoBot;
import fr.xdcc.pi.model.service.MongoBotService;
import fr.xdcc.pi.tasker.bot.FileListUpdaterBot;
import org.jibble.pircbot.IrcException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// The setters are required for the injection of the job parameters
@SuppressWarnings("UnusedDeclaration")
@PersistJobDataAfterExecution
public class FileCheckerJob implements Job {

  private static final Logger LOG = LoggerFactory.getLogger(FileCheckerJob.class);
  private static final int OFFSET_INCREMENT = 5;

  private int max;
  private int offset;

  // I think this is legit in this case as we're waiting for the bot to be fully connected
  @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    LOG.info("Starting job: FileCheckerJob");
    MongoBotService mongoBotService = new MongoBotService();
    long mongoBotsCount = mongoBotService.count();
    long remainingBotsToUpdate = mongoBotsCount - offset;
    long nbTasksToAchieve = remainingBotsToUpdate < OFFSET_INCREMENT ?
        remainingBotsToUpdate : OFFSET_INCREMENT;

    FileListUpdaterBot fileListUpdaterBot = new FileListUpdaterBot("pi-updater", (int) nbTasksToAchieve);
    // fileListUpdaterBot.setVerbose(true);

    try {
      fileListUpdaterBot.connect("irc.otaku-irc.fr");
      fileListUpdaterBot.joinChannel("#serial_us");
      synchronized (fileListUpdaterBot) {
        fileListUpdaterBot.wait();
      }
    } catch (IOException | IrcException e) {
      LOG.debug("Failed to connect the bot to the server: {}", e.getMessage());
    } catch (InterruptedException e) {
      LOG.warn(e.getMessage());
    }

    Iterable<MongoBot> mongoBots = mongoBotService.paginate(max, offset);
    mongoBots.forEach(bot -> fileListUpdaterBot.sendMessage(bot.getName(), "xdcc send -1"));

    int nextOffset = (max + offset) >= mongoBotsCount ? 0 : (offset + OFFSET_INCREMENT);
    jobExecutionContext.getJobDetail().getJobDataMap().put("offset", nextOffset);
    LOG.info("End of job: FileCheckerJob");
  }

  public void setMax(int max) {
    this.max = max;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }
}
