package fr.xdcc.api.tasker.bot;

import fr.xdcc.api.tasker.service.TaskerService;
import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.ReplyConstants;
import org.jibble.pircbot.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Custom implementation of PircBot.
 * This solution is not final and will be removed when the issue
 * with FileTransfer of PircBotX library will be fixed
 */
public class FileListUpdaterBot extends PircBot {

  public FileListUpdaterBot(TaskerService taskerService, int nbTasksToAchieve) {
    setName("api-updater");
    setLogin("api-updater");
    setAutoNickChange(true);
    this.nbTasksToAchieve = nbTasksToAchieve;
    this.taskerService = taskerService;
    legitSenderTagList = new Properties().load().getProperty("xdcc.allowed-senders").split(",");

    makeBaseDir();
  }

  @Override
  protected void onIncomingFileTransfer(DccFileTransfer dccFileTransfer) {
    if (isSenderLegit(dccFileTransfer)) {
      File file = new File(BASE_DIRECTORY + dccFileTransfer.getFile().getName() + ".txt");
      dccFileTransfer.receive(file, false);
      LOG.info("Accepted file [{}] from {}", file.getAbsolutePath(), dccFileTransfer.getNick());
    } else {
      LOG.info("Rejected file from: {}", dccFileTransfer.getNick());
      dccFileTransfer.close();
    }
  }

  @Override
  protected synchronized void onUserList(String channel, User[] users) {
    assert channel.equals("#serial_us");
    this.notifyAll();
  }

  @Override
  protected synchronized void onFileTransferFinished(DccFileTransfer dccFileTransfer, Exception e) {
    dccFileTransfer.close();
    if (e != null) {
      LOG.debug("Transfer went wrong: {}", e.getMessage());
    } else {
      LOG.info("Transfer completed: [{}]", dccFileTransfer.getFile().getAbsolutePath());
      taskerService.updateAvailableFiles(dccFileTransfer.getFile(), dccFileTransfer.getNick());
    }
    registerNewTaskAchieved();
  }

  @Override
  protected synchronized void onServerResponse(int code, String response) {
    if (code == ReplyConstants.ERR_NOSUCHNICK) {
      LOG.debug("Failed contacting remote bot: {}", response);
      registerNewTaskAchieved();
    }
  }

  @Override
  protected void onPrivateMessage(String senderNick, String login, String hostname, String message) {
    LOG.info("Message from <{}>: [{}]", senderNick, message);
  }

  @Override
  protected void onConnect() {
    LOG.info("FileListUpdaterBot connected to server");
  }

  @Override
  protected void onDisconnect() {
    LOG.info("FileListUpdaterBot disconnected from server");
    dispose();
  }

  private boolean isSenderLegit(DccFileTransfer dccFileTransfer) {
    String senderNick = dccFileTransfer.getNick();
    for (String legitSenderTag : legitSenderTagList) {
      if (senderNick.startsWith(legitSenderTag)) {
        return true;
      }
    }

    return false;
  }

  private void registerNewTaskAchieved() {
    ++nbTasksAchieved;
    if (nbTasksAchieved == nbTasksToAchieve) {
      LOG.info("Bot finished all his tasks, be freed!");
      disconnect();
    }
  }

  private void makeBaseDir() {
    File baseDir = new File(BASE_DIRECTORY);
    if (!baseDir.exists() && !baseDir.mkdir()) {
      LOG.warn("Could not create base directory");
      System.exit(1);
    }
  }

  private TaskerService taskerService;
  private int nbTasksToAchieve;
  private int nbTasksAchieved = 0;
  private String[] legitSenderTagList;
  private static final Logger LOG = LoggerFactory.getLogger(FileListUpdaterBot.class);
  private static final String BASE_DIRECTORY = "lists" + File.separator;
}
