package xdcc.web.resource.bot;

import fr.vter.xdcc.search.SearchBus;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Resource;
import xdcc.model.bot.Bot;
import xdcc.search.bot.AllTheBotsSearch;

import javax.inject.Inject;

@Resource
public class BotsResource {

  @Inject
  public BotsResource(SearchBus searchBus) {
    this.searchBus = searchBus;
  }

  @Get("/bot")
  public Iterable<Bot> list() {
    return searchBus.sendAndWaitResponse(new AllTheBotsSearch()).data();
  }

  private final SearchBus searchBus;
}
