package fr.xdcc.api.infrastructure.persistence.mongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

public class MongoManager {

  private static final Logger LOG = LoggerFactory.getLogger(MongoManager.class);

  public static void closeSession() {
    Singleton.INSTANCE.client.close();
  }

  public static MongoCollection getCollection(String collectionName) {
    return Singleton.INSTANCE.jongo.getCollection(collectionName);
  }

  private static enum Singleton {
    INSTANCE;

    private Singleton() {
      try {
        client = new MongoClient();
        DB db = client.getDB("xdccapp");
        jongo = new Jongo(db);
      } catch (UnknownHostException e) {
        LOG.warn("Could not establish the connection to the database: {}", e.getMessage());
      }
    }

    private MongoClient client = null;
    private Jongo jongo = null;
  }
}
