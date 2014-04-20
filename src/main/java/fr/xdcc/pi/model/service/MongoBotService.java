package fr.xdcc.pi.model.service;

import fr.xdcc.pi.model.bot.MongoBot;
import fr.xdcc.pi.model.persistence.MongoManager;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MongoBotService {

  private static final Logger LOG = LoggerFactory.getLogger(MongoBotService.class);

  private MongoCollection mongoBotCollection;

  public MongoBotService() {
    mongoBotCollection = MongoManager.getCollection(MongoBot.COLLECTION_NAME);
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
   */
  public MongoBot get(ObjectId mongoBotId) {
    return mongoBotCollection.findOne(mongoBotId).as(MongoBot.class);
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
   * Returns the names of the MongoBots whose name is contained in the given list
   * @param botNameList a list of MongoBots name
   * @return an Iterable of the Mongobots whose name is contained in the given list
   */
  public Iterable<MongoBot> getBotsIn(List<String> botNameList) {
    return mongoBotCollection.find(
        "{name: {$in: #}}", botNameList
    ).projection("{fileSet: 0}").as(MongoBot.class);
  }
}
