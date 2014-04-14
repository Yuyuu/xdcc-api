package fr.xdcc.pi.tasker.bot;

import fr.xdcc.pi.tasker.service.TaskerService;
import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.PircBot;
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

  private static final Logger LOG = LoggerFactory.getLogger(FileListUpdaterBot.class);
  private static final String BASE_DIRECTORY = System.getProperty("user.home") +
      File.separator + "Downloads" + File.separator + "xdcc" + File.separator;

  private TaskerService taskerService;

  private int nbTasksToAchieve;
  private int nbTasksAchieved;
  private String[] legitSenderTagList;

  public FileListUpdaterBot(String name, int nbTasksToAchieve) {
    setName(name);
    setAutoNickChange(true);
    this.nbTasksToAchieve = nbTasksToAchieve;
    nbTasksAchieved = 0;
    taskerService = new TaskerService();
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
    ++nbTasksAchieved;
    if (e != null) {
      LOG.debug("Transfer went wrong: {}", e.getMessage());
    } else {
      LOG.info("Transfer completed: [{}]", dccFileTransfer.getFile().getAbsolutePath());
      taskerService.updateAvailableFiles(dccFileTransfer.getFile(), dccFileTransfer.getNick());
    }
    if (nbTasksAchieved == nbTasksToAchieve && isConnected()) {
      disconnect();
      dispose();
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
    LOG.debug("FileListUpdaterBot disconnected from server");
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

  private void makeBaseDir() {
    File baseDir = new File(BASE_DIRECTORY);
    if (!baseDir.exists() && !baseDir.mkdir()) {
      LOG.warn("Could not create base directory");
      System.exit(1);
    }
  }

  public void setTaskerService(TaskerService taskerService) {
    this.taskerService = taskerService;
  }
}
