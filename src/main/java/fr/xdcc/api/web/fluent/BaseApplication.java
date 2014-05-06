package fr.xdcc.api.web.fluent;

import net.codestory.http.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseApplication {

  protected abstract Configuration routes();

  protected void start() throws Exception {
    LOG.info("Starting application");
  }

  protected static final Logger LOG = LoggerFactory.getLogger(BaseApplication.class);
}
