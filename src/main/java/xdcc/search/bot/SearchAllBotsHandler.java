package xdcc.search.bot;

import com.google.common.collect.Lists;
import fr.vter.xdcc.search.JongoSearchHandler;
import org.jongo.Jongo;
import xdcc.model.bot.BotMetadata;

public class SearchAllBotsHandler extends JongoSearchHandler<AllTheBotsSearch, Iterable<BotMetadata>> {

  @Override
  protected Iterable<BotMetadata> execute(AllTheBotsSearch search, Jongo jongo) {
    return Lists.newArrayList((Iterable<BotMetadata>) jongo.getCollection("bot").find().as(BotMetadata.class));
  }
}
