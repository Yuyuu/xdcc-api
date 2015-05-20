package xdcc.web.resource.bot;

import fr.vter.xdcc.search.SearchBus;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Resource;
import net.codestory.http.errors.NotFoundException;
import xdcc.model.bot.Bot;
import xdcc.search.bot.BotDetailsSearch;

import javax.inject.Inject;

@Resource
public class BotResource {

  @Inject
  public BotResource(SearchBus searchBus) {
    this.searchBus = searchBus;
  }

  @Get("/bot/:id")
  public Bot show(String id) {
    final Bot bot = searchBus.sendAndWaitResponse(new BotDetailsSearch(id)).data();
    return NotFoundException.notFoundIfNull(bot);
  }

  private final SearchBus searchBus;
}
