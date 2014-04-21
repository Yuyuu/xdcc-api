package fr.xdcc.pi.web.resource;

import com.google.common.collect.Lists;
import fr.xdcc.pi.model.MongoBot;
import fr.xdcc.pi.persistence.mongo.MongoBotService;
import fr.xdcc.pi.web.marshaller.Format;
import fr.xdcc.pi.web.marshaller.Marshaller;
import fr.xdcc.pi.web.marshaller.MongoBotMarshaller;
import net.codestory.http.annotations.Get;

import java.util.List;
import java.util.Map;

public class MongoBotResource extends AbstractResource {

  private MongoBotService mongoBotService = new MongoBotService();
  private Marshaller<MongoBot> mongoBotMarshaller = new MongoBotMarshaller();

  @Get("/bot")
  public List<Map<String, Object>> list() {
    Iterable<MongoBot> mongoBots = mongoBotService.list();
    List<Map<String, Object>> botRepresentationList = Lists.newArrayList();
    mongoBots.forEach( bot ->
        botRepresentationList.add(mongoBotMarshaller.marshall(bot, Format.SHORT))
    );
    return botRepresentationList;
  }

  @Get("/bot/:id")
  public Map<String, Object> show(String id) {
    MongoBot mongoBot = mongoBotService.get(parseObjectId(id));
    return mongoBotMarshaller.marshall(mongoBot, Format.FULL);
  }
}
