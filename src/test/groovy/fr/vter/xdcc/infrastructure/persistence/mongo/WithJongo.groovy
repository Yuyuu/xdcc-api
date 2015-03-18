package fr.vter.xdcc.infrastructure.persistence.mongo

import com.github.fakemongo.Fongo
import com.mongodb.DB
import org.jongo.Jongo
import org.jongo.MongoCollection
import org.junit.rules.ExternalResource

class WithJongo extends ExternalResource {
  private Jongo jongo
  private Fongo fongo

  @Override
  protected void before() throws Throwable {
    fongo = new Fongo("Test")
    jongo = new Jongo(db())
  }

  public jongo() {
    return jongo;
  }

  public MongoCollection collection(String collection) {
    return jongo.getCollection(collection);
  }

  private DB db() {
    return fongo.getDB("xdcc-test");
  }

  @Override
  protected void after() {
    fongo.dropDatabase("xdcc-test");
  }
}
