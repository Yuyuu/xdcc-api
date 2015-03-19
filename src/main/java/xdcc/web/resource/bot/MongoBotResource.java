package xdcc.web.resource.bot;

import com.google.common.collect.Lists;
import fr.vter.xdcc.infrastructure.persistence.mongo.MongoBotService;
import fr.vter.xdcc.model.MongoBot;
import net.codestory.http.Context;
import net.codestory.http.annotations.AllowOrigin;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;
import xdcc.web.marshaller.Format;
import xdcc.web.marshaller.Marshaller;
import xdcc.web.marshaller.MongoBotMarshaller;
import xdcc.web.resource.AbstractResource;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Prefix("/xdcc")
public class MongoBotResource extends AbstractResource {

  @Inject
  public MongoBotResource(MongoBotService mongoBotService) {
    this.mongoBotService = mongoBotService;
  }

  @Get("/bot")
  @AllowOrigin("http://xdcc-webapp.herokuapp.com")
  public List<Map<String, Object>> list(Context context) {
    String formatParameter = context.get("format");
    Format format = (formatParameter != null) ? Format.parseValue(formatParameter) : Format.SHORT;

    Iterable<MongoBot> mongoBots = mongoBotService.list();
    List<Map<String, Object>> botRepresentationList = Lists.newArrayList();
    mongoBots.forEach( bot ->
        botRepresentationList.add(mongoBotMarshaller.marshall(bot, format))
    );

    return botRepresentationList;
  }

  @Get("/bot/:id")
  @AllowOrigin("http://xdcc-webapp.herokuapp.com")
  public Map<String, Object> show(String id, Context context) {
    String formatParameter = context.get("format");
    Format format = (formatParameter != null) ? Format.parseValue(formatParameter) : Format.FULL;

    MongoBot mongoBot = mongoBotService.get(parseObjectId(id));
    return mongoBotMarshaller.marshall(mongoBot, format);
  }

  private final MongoBotService mongoBotService;
  private Marshaller<MongoBot> mongoBotMarshaller = new MongoBotMarshaller();
}
