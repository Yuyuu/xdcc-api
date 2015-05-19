package fr.vter.xdcc.search

import com.github.fakemongo.Fongo
import com.gmongo.GMongo
import com.mongodb.DB
import com.mongodb.DBCollection
import org.jongo.Jongo
import org.junit.rules.ExternalResource

class WithJongo extends ExternalResource {

  private Jongo jongo
  private Fongo fongo

  @Override
  protected void before() throws Throwable {
    fongo = new Fongo("Test")
    jongo = new Jongo(fongo.getDB("test"))
  }

  public jongo() {
    return jongo;
  }

  public DB db() {
    return new GMongo(fongo.mongo).getDB("test");
  }

  public DBCollection collection(String collection) {
    return db().getCollection(collection);
  }

  @Override
  protected void after() {
    fongo.dropDatabase("test");
  }
}
