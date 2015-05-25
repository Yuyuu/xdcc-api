package xdcc.search;

import fr.vter.xdcc.search.Search;
import xdcc.model.bot.BotMetadata;

public class BotsWhichNicknameContainsSearch extends Search<Iterable<BotMetadata>> {

  public BotsWhichNicknameContainsSearch(String query) {
    pieceOfNickname = query;
  }

  public final String pieceOfNickname;
}
