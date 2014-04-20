package fr.xdcc.pi.model.service

import com.github.fakemongo.Fongo
import com.mongodb.DB
import fr.xdcc.pi.model.bot.MongoBot
import fr.xdcc.pi.model.file.ConcreteFile
import org.jongo.Jongo
import org.jongo.MongoCollection
import spock.lang.Specification

@SuppressWarnings("GroovyAccessibility")
class MongoBotServiceTest extends Specification {

  MongoBotService mongoBotService
  MongoCollection mongoCollection

  MongoBot mongoBot1
  MongoBot mongoBot2
  MongoBot mongoBot3
  MongoBot mongoBot4
  MongoBot mongoBot5

  def setup() {
    Fongo fongo = new Fongo("mongo server 1")
    DB db = fongo.getDB("xdcc-test")
    Jongo jongo = new Jongo(db)
    mongoCollection = jongo.getCollection("mongobots")

    mongoBot1 = new MongoBot("bot 1")
    mongoBot2 = new MongoBot("bot 2")
    mongoBot3 = new MongoBot("bot 3")
    mongoBot4 = new MongoBot("bot 4")
    mongoBot5 = new MongoBot("bot 5")

    mongoCollection.insert(mongoBot1, mongoBot2, mongoBot3, mongoBot4, mongoBot5)

    mongoBotService = new MongoBotService()
    mongoBotService.mongoBotCollection = mongoCollection
  }

  def "list"() {
    when: "calling list method"
    def result = mongoBotService.list()

    then: "the returned Iterable should contain the 5 MongoBots"
    result.iterator().next() == mongoBot1
    result.iterator().next() == mongoBot2
    result.iterator().next() == mongoBot3
    result.iterator().next() == mongoBot4
    result.iterator().next() == mongoBot5
    !result.iterator().hasNext()
  }

  def "paginate"() {
    when: "calling paginate with max = 2 & no offset"
    def result = mongoBotService.paginate(2, 0)

    then: "an Iterable containing the MongoBots 1 & 2 should be returned"
    result.iterator().next() == mongoBot1
    result.iterator().next() == mongoBot2
    !result.iterator().hasNext()

    when: "calling paginate with max = 2 & offset = 2"
    result = mongoBotService.paginate(2, 2)

    then: "an Iterable containing the MongoBots 3 & 4 should be returned"
    result.iterator().next() == mongoBot3
    result.iterator().next() == mongoBot4
    !result.iterator().hasNext()

    when: "calling paginate with max = 2 & offset = 4"
    result = mongoBotService.paginate(2, 4)

    then: "an Iterable containing the MongoBot 5 should be returned"
    result.iterator().next() == mongoBot5
    !result.iterator().hasNext()
  }

  def "get - Bot exists"() {
    expect: "get method to returned MongoBot 5 when its name is passed"
    mongoBotService.get(mongoBot5.name) == mongoBot5
  }

  def "get - Bot is not saved"() {
    when: "calling get with a name not matching any saved MongoBot"
    def result = mongoBotService.get("Random")

    then: "a new instantiated but unsaved MongoBot should be returned"
    !result.id
    result.getName() == "Random"
  }

  def "insert"() {
    given: "a new MongoBot"
    MongoBot mongoBot = new MongoBot('NewBot')

    when: "inserting the bot"
    mongoBotService.insert(mongoBot)
    def result = mongoBotService.get(mongoBot.name)

    then: "the bot should be saved in the database"
    result.id
    result == mongoBot
  }

  def "update"() {
    given: "a new MongoBot with two ConcreteFiles"
    MongoBot mongoBot = new MongoBot('NewBot')
    mongoBot.fileSet.add(new ConcreteFile("#1", "First.Episode.avi"))
    mongoBot.fileSet.add(new ConcreteFile("#2", "Second.Episode.avi"))
    mongoCollection.insert(mongoBot)

    when: "Adding another ConcreteFile to the bot and calling update method"
    mongoBot.fileSet.add(new ConcreteFile("#3", "Third.Episode.avi"))
    mongoBotService.update(mongoBot)
    def result = mongoBotService.get(mongoBot.name)

    then: "the bot should have been updated in the database"
    result == mongoBot
    result.fileSet.size() == 3
  }

  def "count"() {
    expect: "count method to return 5"
    mongoBotService.count() == 5
  }

  def "getBotsIn"() {
    given: "a list containing the name of MongoBots 1 & 4 and two random strings"
    def list = [mongoBot1.name, "blah", mongoBot4.name, "rdm"]

    when: "calling getBotsIn method"
    Iterable<MongoBot> result = mongoBotService.getBotsIn(list)

    then: "an Interable with only MongoBots 1 & 4 should be returned"
    result.iterator().next().id == mongoBot1.id
    result.iterator().next().id == mongoBot4.id
    !result.iterator().hasNext()
  }
}
