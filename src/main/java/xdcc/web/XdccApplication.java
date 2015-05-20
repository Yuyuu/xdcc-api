package xdcc.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import fr.vter.xdcc.web.fluent.BaseApplication;
import net.codestory.http.Configuration;
import net.codestory.http.injection.GuiceAdapter;
import net.codestory.http.payload.Payload;
import xdcc.web.configuration.GuiceConfiguration;
import xdcc.web.configuration.XdccExtensions;

import java.util.Optional;

public class XdccApplication extends BaseApplication {

  public XdccApplication() {
    injector = Guice.createInjector(stage(), new GuiceConfiguration());
  }

  private static Stage stage() {
    final Optional<String> env = Optional.ofNullable(System.getenv("env"));
    LOGGER.info("Configuration mode: {}", env.orElse("dev"));
    if (env.orElse("dev").equals("dev")) {
      return Stage.DEVELOPMENT;
    }
    return Stage.PRODUCTION;
  }

  @Override
  protected Configuration routes() {
    return routes -> routes
        .setExtensions(new XdccExtensions())
        .setIocAdapter(new GuiceAdapter(injector))
        .get("/", Payload.ok())
        .autoDiscover("xdcc.web.resource");
  }

  private final Injector injector;
}
