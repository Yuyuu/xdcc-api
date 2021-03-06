package xdcc.infrastructure.persistence.mongo.mapping;

import org.mongolink.domain.mapper.AggregateMap;
import xdcc.model.bot.Pack;

public class PackMapping extends AggregateMap<Pack> {

  @Override
  public void map() {
    property().onField("position");
    property().onField("title");
    property().onField("botId");
  }
}
