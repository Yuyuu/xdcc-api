package xdcc.web.resource.search;

import fr.vter.xdcc.search.SearchBus;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Resource;
import xdcc.model.bot.BotMetadata;
import xdcc.search.BotsWhichNicknameContainsSearch;

import javax.inject.Inject;

@Resource
public class SearchResource {

  @Inject
  public SearchResource(SearchBus searchBus) {
    this.searchBus = searchBus;
  }

  @Get("/search?q=:query")
  public Iterable<BotMetadata> search(String query) {
    return searchBus.sendAndWaitResponse(new BotsWhichNicknameContainsSearch(query)).data();
  }

  private final SearchBus searchBus;
}
