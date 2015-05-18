package xdcc.infrastructure.persistence.mongo.mapping;

import org.mongolink.domain.mapper.AggregateMap;
import xdcc.model.bot.Bot;

public class BotMapping extends AggregateMap<Bot> {

  @Override
  public void map() {
    id().onField("_id").natural();
    property().onField("nickname");
    collection().onField("packs");
  }
}
