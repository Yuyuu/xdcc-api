package xdcc.web.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import xdcc.model.bot.Pack;

import java.io.IOException;

public class PackSerializer extends JsonSerializer<Pack> {

  @Override
  public void serialize(Pack value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeStartObject();
    gen.writeNumberField("position", value.position());
    gen.writeStringField("title", value.title());
    gen.writeStringField("botId", value.botId().toHexString());
    gen.writeEndObject();
  }
}
