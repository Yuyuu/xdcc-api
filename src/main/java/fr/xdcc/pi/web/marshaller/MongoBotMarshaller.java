package fr.xdcc.pi.web.marshaller;

import com.google.common.collect.Maps;
import fr.xdcc.pi.model.bot.mongo.MongoBot;

import java.util.Map;

public class MongoBotMarshaller implements Marshaller<MongoBot> {

  @Override
  public Map<String, Object> marshall(MongoBot entity) {
    Map<String, Object> mongoBotRepresentation = Maps.newLinkedHashMap();
    mongoBotRepresentation.put("id", entity.getId().toStringMongod());
    mongoBotRepresentation.put("name", entity.getName());
    mongoBotRepresentation.put("fileSet", entity.getFileSet());
    return mongoBotRepresentation;
  }
}
