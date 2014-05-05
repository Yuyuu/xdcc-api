package xdcc.web.resource.bot;

import com.google.common.collect.Lists;
import fr.xdcc.api.model.MongoBot;
import fr.xdcc.api.infrastructure.persistence.mongo.MongoBotService;
import xdcc.web.marshaller.Format;
import xdcc.web.marshaller.Marshaller;
import xdcc.web.marshaller.MongoBotMarshaller;
import xdcc.web.resource.AbstractResource;
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
