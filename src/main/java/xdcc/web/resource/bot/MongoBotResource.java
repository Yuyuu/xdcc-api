package xdcc.web.resource.bot;

import com.google.common.collect.Lists;
import fr.xdcc.api.infrastructure.persistence.mongo.MongoBotService;
import fr.xdcc.api.model.MongoBot;
import net.codestory.http.Context;
import net.codestory.http.annotations.Get;
import xdcc.web.marshaller.Format;
import xdcc.web.marshaller.Marshaller;
import xdcc.web.marshaller.MongoBotMarshaller;
import xdcc.web.resource.AbstractResource;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class MongoBotResource extends AbstractResource {

  @Inject
  public MongoBotResource(MongoBotService mongoBotService) {
    this.mongoBotService = mongoBotService;
  }

  @Get("/bot")
  public List<Map<String, Object>> list(Context context) {
    String formatParameter = context.get("format");
    Format format = (formatParameter != null) ? Format.parseValue(formatParameter) : Format.SHORT;

    Iterable<MongoBot> mongoBots = mongoBotService.list();
    List<Map<String, Object>> botRepresentationList = Lists.newArrayList();
    mongoBots.forEach( bot ->
        botRepresentationList.add(mongoBotMarshaller.marshall(bot, format))
    );

    addAccessControlHeader(context);
    return botRepresentationList;
  }

  @Get("/bot/:id")
  public Map<String, Object> show(String id, Context context) {
    String formatParameter = context.get("format");
    Format format = (formatParameter != null) ? Format.parseValue(formatParameter) : Format.FULL;

    addAccessControlHeader(context);

    MongoBot mongoBot = mongoBotService.get(parseObjectId(id));
    return mongoBotMarshaller.marshall(mongoBot, format);
  }

  private final MongoBotService mongoBotService;
  private Marshaller<MongoBot> mongoBotMarshaller = new MongoBotMarshaller();
}
