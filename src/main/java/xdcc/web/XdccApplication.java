package xdcc.web;

import fr.vter.xdcc.web.fluent.BaseApplication;
import net.codestory.http.Configuration;
import net.codestory.http.payload.Payload;

public class XdccApplication extends BaseApplication {

  @Override
  protected Configuration routes() {
    return routes -> routes
        .get("/", Payload.ok());
  }
}
