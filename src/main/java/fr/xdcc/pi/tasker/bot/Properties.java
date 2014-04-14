package fr.xdcc.pi.tasker.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class Properties {

  private static final Logger LOG = LoggerFactory.getLogger(Properties.class);

  public java.util.Properties load() {
    return Config.INSTANCE.properties;
  }

  private static enum Config {
    INSTANCE;

    private Config() {
      InputStream stream = getClass().getClassLoader().getResourceAsStream("sender.properties");
      properties = new java.util.Properties();
      try {
        properties.load(stream);
        stream.close();
      } catch (IOException e) {
        LOG.warn("Error loading init properties: {}", e.getMessage());
        System.exit(1);
      }
    }

    private final java.util.Properties properties;
  }
}
