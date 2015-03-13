package fr.xdcc.api.tasker.service;

import com.google.common.collect.Sets;
import fr.xdcc.api.infrastructure.persistence.mongo.MongoBotService;
import fr.xdcc.api.model.Bot;
import fr.xdcc.api.model.ConcreteFile;
import fr.xdcc.api.model.MongoBot;
import fr.xdcc.api.tasker.parser.XdccListFileParser;
import fr.xdcc.api.tasker.parser.XdccWebsiteParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskerService {

  @Inject
  public TaskerService(MongoBotService mongoBotService) {
    this.mongoBotService = mongoBotService;
  }

  /**
   * Updates the list of available files of a bot if it was changed
   * @param  updatedList the file containing the freshly received list
   * @param botNickname the name of the bot who sent the list
   */
  public void updateAvailableFiles(File updatedList, String botNickname) {
    Map<String, String> packMap = xdccListFileParser.parse(updatedList);
    internalUpdate(botNickname, packMap);
  }

  public void updateAvailableFiles(String botNickname) {
    Map<String, String> packMap = xdccWebsiteParser.parse(botNickname);
    internalUpdate(botNickname, packMap);
  }

  /**
   * Searches for the bots missing in the database and adds them
   * @param botNameList the list of bot names that should be in the database
   */
  public void updateAvailableBots(List<String> botNameList) {
    Iterable<MongoBot> savedBotList = mongoBotService.getBotsIn(botNameList);
    savedBotList.forEach(bot -> botNameList.remove(bot.getName()));

    List<MongoBot> botToUpdateList = botNameList.stream().map(MongoBot::new).collect(Collectors.toList());

    botToUpdateList.stream().forEach(mongoBotService::insert);
  }

  private void internalUpdate(String botNickname, Map<String, String> packMap) {
    LinkedHashSet<ConcreteFile> concreteFileSet = Sets.newLinkedHashSet();

    // TODO Meh..
    packMap.entrySet().stream().forEach(entry ->
            concreteFileSet.add(new ConcreteFile(entry.getKey(), entry.getValue()))
    );

    Bot bot = mongoBotService.findByName(botNickname);
    if (bot == null) {
      bot = new MongoBot(botNickname);
    }

    if (!concreteFileSet.equals(bot.getFileSet())) {
      bot.setFileSet(concreteFileSet);
      bot.setLastUpdated(new Date());
      LOG.info("Bot {} got new files", bot.getName());
    } else {
      LOG.info("Files of bot {} remain unchanged", bot.getName());
    }

    bot.setLastChecked(new Date());
    mongoBotService.update((MongoBot) bot);
  }

  private MongoBotService mongoBotService;
  private XdccListFileParser xdccListFileParser = new XdccListFileParser();
  private XdccWebsiteParser xdccWebsiteParser = new XdccWebsiteParser();
  private static final Logger LOG = LoggerFactory.getLogger(TaskerService.class);
}
