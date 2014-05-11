package xdcc.web.configuration;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import fr.xdcc.api.tasker.scheduler.GuiceJobFactory;
import fr.xdcc.api.tasker.service.TaskerService;
import org.jongo.Jongo;
import org.quartz.spi.JobFactory;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkState;

public class GuiceConfiguration extends AbstractModule {

  @Override
  protected void configure() {
    Names.bindProperties(binder(), properties());
    bind(JobFactory.class).to(GuiceJobFactory.class);
    bind(TaskerService.class).asEagerSingleton();
  }

  @SuppressWarnings("EmptyCatchBlock")
  private Properties properties() {
    URL url = Resources.getResource(
        "env/" + Optional.ofNullable(System.getenv("env")).orElse("dev") + "/application.properties"
    );
    ByteSource inputSupplier = Resources.asByteSource(url);
    Properties properties = new Properties();
    try {
      properties.load(inputSupplier.openStream());
    } catch (IOException e) {
    }
    return properties;
  }

  @Provides
  @Singleton
  public Jongo jongo(MongoDBConfiguration configurationMongoDb) throws UnknownHostException {
    final MongoClient mongoClient = new MongoClient(configurationMongoDb.host, configurationMongoDb.port);
    final DB db = mongoClient.getDB(configurationMongoDb.name);
    if (configurationMongoDb.withDBAuth()) {
      checkState(db.authenticate(configurationMongoDb.user, configurationMongoDb.password.toCharArray()));
    }
    return new Jongo(db);
  }
}
