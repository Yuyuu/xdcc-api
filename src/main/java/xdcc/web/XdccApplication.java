package xdcc.web;

import fr.xdcc.api.web.fluent.BaseApplication;
import net.codestory.http.Configuration;
import xdcc.web.filter.LogRequestFilter;
import xdcc.web.resource.HomeResource;
import xdcc.web.resource.bot.MongoBotResource;

public class XdccApplication extends BaseApplication {

  @Override
  protected Configuration routes() {
    return routes -> routes
        .filter(LogRequestFilter.class)
        .add(HomeResource.class)
        .add(MongoBotResource.class);
  }
}
