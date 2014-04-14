package fr.xdcc.pi.web.resource;

import fr.xdcc.pi.model.bot.MongoBot;
import fr.xdcc.pi.model.file.ConcreteFile;
import fr.xdcc.pi.model.service.MongoBotService;
import net.codestory.http.annotations.Get;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class MongoBotResource {

  private MongoBotService mongoBotService;

  public MongoBotResource() {
    mongoBotService = new MongoBotService();
  }

  @Get("/bot")
  public List<String> list() {
    List<String> botNameRepresentationList = new ArrayList<>();
    Iterable<MongoBot> mongoBots = mongoBotService.list();
    mongoBots.forEach(bot -> botNameRepresentationList.add(bot.getName()));
    return botNameRepresentationList;
  }

  @Get("/bot/:botName")
  public LinkedHashSet<ConcreteFile> show(String botName) {
    MongoBot mongoBot = mongoBotService.get(botName);
    return mongoBot.getFileSet();
  }

  @Get("/bot/:botName/count")
  public int count(String botName) {
    MongoBot mongoBot = mongoBotService.get(botName);
    return mongoBot.getFileSet().size();
  }
}
