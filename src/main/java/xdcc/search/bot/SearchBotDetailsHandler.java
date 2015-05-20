package xdcc.search.bot;

import fr.vter.xdcc.search.JongoSearchHandler;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import xdcc.model.bot.Bot;

public class SearchBotDetailsHandler extends JongoSearchHandler<BotDetailsSearch, Bot> {

  @Override
  protected Bot execute(BotDetailsSearch search, Jongo jongo) {
    final ObjectId botId = new ObjectId(search.id);
    return jongo.getCollection("bot").findOne(botId).as(Bot.class);
  }
}
