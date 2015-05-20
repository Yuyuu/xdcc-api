package xdcc.search.bot;

import fr.vter.xdcc.search.Search;
import xdcc.model.bot.Bot;

public class BotDetailsSearch extends Search<Bot> {

  public BotDetailsSearch(String id) {
    this.id = id;
  }

  public final String id;
}
