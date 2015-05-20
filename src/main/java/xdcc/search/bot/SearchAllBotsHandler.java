package xdcc.search.bot;

import com.google.common.collect.Lists;
import fr.vter.xdcc.search.JongoSearchHandler;
import org.jongo.Jongo;
import xdcc.model.bot.Bot;

public class SearchAllBotsHandler extends JongoSearchHandler<AllTheBotsSearch, Iterable<Bot>> {

  @Override
  protected Iterable<Bot> execute(AllTheBotsSearch search, Jongo jongo) {
    return Lists.newArrayList((Iterable<Bot>) jongo.getCollection("bot").find().as(Bot.class));
  }
}
