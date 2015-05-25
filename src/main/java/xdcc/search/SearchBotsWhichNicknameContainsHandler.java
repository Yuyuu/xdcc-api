package xdcc.search;

import com.google.common.collect.Lists;
import fr.vter.xdcc.search.JongoSearchHandler;
import org.jongo.Jongo;
import xdcc.model.bot.BotMetadata;

public class SearchBotsWhichNicknameContainsHandler extends JongoSearchHandler<BotsWhichNicknameContainsSearch, Iterable<BotMetadata>> {

  @Override
  protected Iterable<BotMetadata> execute(BotsWhichNicknameContainsSearch search, Jongo jongo) {
    Iterable<BotMetadata> bots = jongo.getCollection("bot").find(
        "{nickname: {$regex: #}}", "(?i).*" + search.pieceOfNickname + ".*"
    ).as(BotMetadata.class);
    return Lists.newArrayList(bots);
  }
}
