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
import java.io.IOException;
import java.net.URL;
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

  public void updateAvailableFiles(File updatedList, String botNickname) {
    Map<String, String> packMap = xdccListFileParser.parse(updatedList);

    Bot bot = mongoBotService.findByName(botNickname);
    if (bot == null) {
      bot = new MongoBot(botNickname);
    }

    internalUpdate(bot, packMap);
  }

  public void updateAvailableFiles(String botNickname) {
    Bot bot = mongoBotService.findByName(botNickname);
    String botStringUrl;

    if (bot == null) {
      bot = new MongoBot(botNickname);
      bot.setUrl(botStringUrl = urlFinder.findBotUrl(botNickname));
    } else {
      botStringUrl = (bot.getUrl() != null) ? bot.getUrl() : urlFinder.findBotUrl(botNickname);
    }

    if (botStringUrl == null) {
      LOG.debug("URL of bot {} could not be found", botNickname);
      return;
    }

    try {
      URL url = new URL(botStringUrl);
      Map<String, String> packMap = xdccWebsiteParser.parse(url.openStream());
      bot.setUrl(botStringUrl);
      internalUpdate(bot, packMap);
    } catch (IOException exception) {
      LOG.info(
          "There was a problem with the URL [{}] of bot {} ==> [{}]",
          botStringUrl,
          botNickname,
          exception.getMessage()
      );
      bot.setUrl(null);
      mongoBotService.update((MongoBot) bot);
    }
  }

  public void updateAvailableBots(List<String> botNameList) {
    Iterable<MongoBot> savedBotList = mongoBotService.getBotsIn(botNameList);
    savedBotList.forEach(bot -> botNameList.remove(bot.getName()));

    List<MongoBot> botToUpdateList = botNameList.stream().map(MongoBot::new).collect(Collectors.toList());

    botToUpdateList.stream().forEach(mongoBotService::insert);
  }

  private void internalUpdate(Bot bot, Map<String, String> packMap) {
    LinkedHashSet<ConcreteFile> concreteFileSet = Sets.newLinkedHashSet();

    // TODO Meh..
    packMap.entrySet().stream().forEach(entry ->
            concreteFileSet.add(new ConcreteFile(entry.getKey(), entry.getValue()))
    );

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
  private UrlFinder urlFinder = new UrlFinder();
  private XdccListFileParser xdccListFileParser = new XdccListFileParser();
  private XdccWebsiteParser xdccWebsiteParser = new XdccWebsiteParser();
  private static final Logger LOG = LoggerFactory.getLogger(TaskerService.class);
}
