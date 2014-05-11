package fr.xdcc.api.tasker.bot;

import fr.xdcc.api.tasker.service.TaskerService;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Custom implementation of PircBot.
 * This solution is not final and will be removed when the issue
 * with FileTransfer of PircBotX library will be fixed
 */
public class SentryBot extends PircBot {

  public SentryBot(TaskerService taskerService) {
    setName("api-sentry");
    setLogin("api-sentry");
    setAutoNickChange(true);
    this.taskerService = taskerService;
    Properties properties = new fr.xdcc.api.tasker.bot.Properties().load();
    senderBotTags = properties.getProperty("xdcc.sender-tag").split(",");
  }

  @Override
  protected void onUserList(String channel, User[] users) {
    assert channel.equals("#serial_us");

    List<User> userList = Arrays.asList(users);
    List<String> senderBotNameList = userList.parallelStream().filter(this::isBotSender).map(
        user -> user.getNick().substring(1)
    ).collect(Collectors.toList());

    if (!senderBotNameList.isEmpty()) {
      taskerService.updateAvailableBots(senderBotNameList);
    } else {
      LOG.debug("No bot sender found on {}", channel);
    }

    disconnect();
  }

  @Override
  protected void onPrivateMessage(String senderNick, String login, String hostname, String message) {
    LOG.info("Message from <{}>: [{}]", senderNick, message);
  }

  @Override
  protected void onConnect() {
    LOG.info("SentryBot connected to server");
  }

  @Override
  protected void onDisconnect() {
    LOG.info("SentryBot disconnected from server");
    dispose();
  }

  private boolean isBotSender(User u) {
    for (String tag : senderBotTags) {
      if (u.getNick().startsWith(tag)) {
        return true;
      }
    }

    return false;
  }

  private TaskerService taskerService;
  private String[] senderBotTags;
  private static final Logger LOG = LoggerFactory.getLogger(SentryBot.class);
}
