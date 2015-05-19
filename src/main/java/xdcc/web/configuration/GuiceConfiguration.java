package xdcc.web.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import fr.vter.xdcc.infrastructure.bus.guice.HandlerScanner;
import fr.vter.xdcc.search.SearchBus;
import fr.vter.xdcc.search.SearchHandler;
import org.jongo.Jongo;

import java.net.UnknownHostException;
import java.util.Optional;

public class GuiceConfiguration extends AbstractModule {

  @Override
  protected void configure() {
    configureSearches();
  }

  private void configureSearches() {
    HandlerScanner.scanPackageAndBind("xdcc.search", SearchHandler.class, binder());
    bind(SearchBus.class).asEagerSingleton();
  }

  @Provides
  @Singleton
  public Jongo jongo() throws UnknownHostException {
    MongoClientURI uri = new MongoClientURI(
        Optional.ofNullable(System.getenv("XDCC_API_MONGO_URI"))
            .orElseThrow(() -> new IllegalStateException("Missing database configuration"))
    );
    MongoClient mongoClient = new MongoClient(uri);
    return new Jongo(mongoClient.getDB(uri.getDatabase()));
  }
}
