package xdcc.web.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import xdcc.model.bot.BotMetadata;

import java.io.IOException;

public class BotMetadataSerializer extends JsonSerializer<BotMetadata> {

  @Override
  public void serialize(BotMetadata value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("id", value.getId().toHexString());
    gen.writeStringField("nickname", value.nickname());
    gen.writeEndObject();
  }
}
