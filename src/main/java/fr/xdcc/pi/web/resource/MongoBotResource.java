package fr.xdcc.pi.web.resource;

import com.google.common.collect.Lists;
import fr.xdcc.pi.model.bot.MongoBot;
import fr.xdcc.pi.model.file.ConcreteFile;
import fr.xdcc.pi.model.service.MongoBotService;
import fr.xdcc.pi.web.marshaller.Marshaller;
import fr.xdcc.pi.web.marshaller.MongoBotMarshaller;
import net.codestory.http.annotations.Get;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class MongoBotResource {

  private MongoBotService mongoBotService;

  public MongoBotResource() {
    mongoBotService = new MongoBotService();
  }

  @Get("/bot")
  public List list() {
    Marshaller<MongoBot> marshaller = new MongoBotMarshaller();
    Iterable<MongoBot> mongoBots = mongoBotService.list();
    List<Map<String, Object>> botRepresentationList = Lists.newArrayList();
    mongoBots.forEach(bot -> botRepresentationList.add(marshaller.marshall(bot)));
    return botRepresentationList;
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
