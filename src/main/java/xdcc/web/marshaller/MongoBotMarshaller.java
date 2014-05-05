package xdcc.web.marshaller;

import com.google.common.collect.Maps;
import fr.xdcc.api.model.MongoBot;

import java.util.Map;

public class MongoBotMarshaller implements Marshaller<MongoBot> {

  @Override
  public Map<String, Object> marshall(MongoBot entity, Format format) {
    switch(format) {
      case SHORT:
        return marshallShort(entity);

      case FULL:
        return marshallFull(entity);

      default:
        throw new IllegalStateException("Format " + format + " is not suported by this marshaller");
    }
  }

  private Map<String, Object> marshallShort(MongoBot mongoBot) {
    Map<String, Object> mongoBotRepresentation = Maps.newLinkedHashMap();
    mongoBotRepresentation.put("id", mongoBot.getId().toStringMongod());
    mongoBotRepresentation.put("name", mongoBot.getName());
    mongoBotRepresentation.put("fileCount", mongoBot.getFileSet().size());
    mongoBotRepresentation.put("lastChecked", mongoBot.getLastChecked());
    mongoBotRepresentation.put("lastUpdated", mongoBot.getLastUpdated());
    return mongoBotRepresentation;
  }

  private Map<String, Object> marshallFull(MongoBot mongoBot) {
    Map<String, Object> mongoBotRepresentation = marshallShort(mongoBot);
    mongoBotRepresentation.put("fileSet", mongoBot.getFileSet());
    return mongoBotRepresentation;
  }
}
