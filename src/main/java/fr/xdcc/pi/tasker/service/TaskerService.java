package fr.xdcc.pi.tasker.service;

import fr.xdcc.pi.model.bot.Bot;
import fr.xdcc.pi.model.bot.MongoBot;
import fr.xdcc.pi.model.file.ConcreteFile;
import fr.xdcc.pi.model.service.MongoBotService;
import fr.xdcc.pi.tasker.parser.Parser;
import fr.xdcc.pi.tasker.parser.XdccListFileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class TaskerService {

  private static final Logger LOG = LoggerFactory.getLogger(TaskerService.class);

  private MongoBotService mongoBotService;
  private Parser parser;

  public TaskerService() {
    mongoBotService = new MongoBotService();
    parser = new XdccListFileParser();
  }

  /**
   * Updates the list of available files of a bot if it was changed
   * @param  updatedList the file containing the freshly received list
   * @param botName the name of the bot who sent the list
   */
  public void updateAvailableFiles(File updatedList, String botName) {
    Map<String, String> packMap = parser.parse(updatedList);
    LinkedHashSet<ConcreteFile> concreteFileSet = new LinkedHashSet<>();

    // TODO Meh..
    packMap.entrySet().forEach(entry ->
        concreteFileSet.add(new ConcreteFile(entry.getKey(), entry.getValue()))
    );

    Bot bot = mongoBotService.get(botName);
    if (!concreteFileSet.equals(bot.getFileSet())) {
      bot.setFileSet(concreteFileSet);
      mongoBotService.update((MongoBot) bot);
    } else {
      LOG.info("Files of bot {} remain unchanged", bot.getName());
    }
  }

  /**
   * Searches for the bots missing in the database and adds them
   * @param botNameList the list of bot names that should be in the database
   */
  public void updateAvailableBots(List<String> botNameList) {
    // TODO : Improve
    Iterable<MongoBot> savedBotList = mongoBotService.getBotsIn(botNameList);
    savedBotList.forEach(bot -> botNameList.remove(bot.getName()));

    List<MongoBot> botToUpdateList = new ArrayList<>();
    botNameList.forEach(botName -> botToUpdateList.add(new MongoBot(botName)));

    botToUpdateList.forEach(mongoBotService::insert);
  }
}
