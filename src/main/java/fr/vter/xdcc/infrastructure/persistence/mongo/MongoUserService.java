package fr.vter.xdcc.infrastructure.persistence.mongo;

import fr.vter.xdcc.model.MongoUser;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * @author Vincent Tertre
 */
public class MongoUserService {

  @Inject
  public MongoUserService(Jongo jongo) {
    mongoUserCollection = jongo.getCollection(MongoUser.COLLECTION_NAME);
  }

  public MongoUser findByLogin(String login) {
    return mongoUserCollection.findOne("{login: #}", login).as(MongoUser.class);
  }

  private MongoCollection mongoUserCollection;
  private final static Logger LOG = LoggerFactory.getLogger(MongoUserService.class);
}
