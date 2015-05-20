package xdcc.web.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import xdcc.model.bot.Bot;
import xdcc.model.bot.Pack;

import java.io.IOException;

public class BotSerializer extends JsonSerializer<Bot> {

  @Override
  public void serialize(Bot value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("id", value.getId().toHexString());
    gen.writeStringField("nickname", value.nickname());
    gen.writeArrayFieldStart("packs");
    for (Pack pack : value.packs()) {
      gen.writeObject(pack);
    }
    gen.writeEndArray();
    gen.writeEndObject();
  }
}
