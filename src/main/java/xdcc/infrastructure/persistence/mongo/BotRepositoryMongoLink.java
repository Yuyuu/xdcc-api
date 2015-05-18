package xdcc.infrastructure.persistence.mongo;

import fr.vter.xdcc.infrastructure.persistence.mongo.MongoLinkRepository;
import org.mongolink.MongoSession;
import xdcc.model.bot.Bot;
import xdcc.model.bot.BotRepository;

public class BotRepositoryMongoLink extends MongoLinkRepository<Bot> implements BotRepository {

  protected BotRepositoryMongoLink(MongoSession session) {
    super(session);
  }
}
