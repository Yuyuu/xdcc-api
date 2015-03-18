package fr.vter.xdcc.infrastructure.persistence.mongo;

import fr.vter.xdcc.model.MissingMongoBotException;
import fr.vter.xdcc.model.MongoBot;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class MongoBotService {

  @Inject
  public MongoBotService(Jongo jongo) {
    mongoBotCollection = jongo.getCollection(MongoBot.COLLECTION_NAME);
  }

  /**
   * Retrieves all the MongoBots from the database
   * @return an Iterable on the retrieved MongoBots
   */
  public Iterable<MongoBot> list() {
    return mongoBotCollection.find().as(MongoBot.class);
  }

  /**
   * Gets a restrained Iterable of Mongobots
   * @param max the maximum size of the returned Iterable
   * @param offset the number of MongoBots to skip when querying the database
   * @return an Iterable of MongoBots
   */
  public Iterable<MongoBot> paginate(int max, int offset) {
    return mongoBotCollection.find().skip(offset).limit(max).as(MongoBot.class);
  }

  /**
   * Retrieves a MongoBot by its id.
   * @param mongoBotId the id of the MongoBot to retrieve
   * @return the retrieved MongoBot
   * @throws fr.vter.xdcc.model.MissingMongoBotException if no MongoBot matches the given id
   */
  public MongoBot get(ObjectId mongoBotId) {
    MongoBot mongoBot =  mongoBotCollection.findOne(mongoBotId).as(MongoBot.class);
    if (mongoBot == null) {
      throw new MissingMongoBotException(mongoBotId);
    }

    return mongoBot;
  }

  /**
   * Retrieves a MongoBot from the database using its name.
   * @param botName the name of the MongoBot to retrieve
   * @return the retrieved MongoBot
   */
  public MongoBot findByName(String botName) {
    return mongoBotCollection.findOne("{name: #}", botName).as(MongoBot.class);
  }

  /**
   * Inserts a MongoBot in the database
   * @param mongoBot the MongoBot to insert
   */
  public void insert(MongoBot mongoBot) {
    LOG.info("Inserting: {}", mongoBot.getName());
    updateLastUpdatedTime(mongoBot);
    mongoBotCollection.insert(mongoBot);
  }

  /**
   * Updates a MongoBot
   * @param mongoBot the MongoBot to update
   */
  public void update(MongoBot mongoBot) {
    LOG.info("Updating: {}", mongoBot.getName());
    mongoBotCollection.save(mongoBot);
  }

  /**
   * Gets the total number of MongoBots in the database
   * @return the total number of MongoBots in the database
   */
  public long count() {
    return mongoBotCollection.count();
  }

  /**
   * Returns the MongoBots whose name is contained in the given list
   * @param botNameList a list of MongoBots name
   * @return an Iterable of the Mongobots whose name is contained in the given list
   */
  public Iterable<MongoBot> getBotsIn(List<String> botNameList) {
    return mongoBotCollection.find(
        "{name: {$in: #}}", botNameList
    ).projection("{fileSet: 0}").as(MongoBot.class);
  }

  private void updateLastUpdatedTime(MongoBot mongoBot) {
    mongoBot.setLastUpdated(new Date());
  }

  private MongoCollection mongoBotCollection;
  private final static Logger LOG = LoggerFactory.getLogger(MongoBotService.class);
}
