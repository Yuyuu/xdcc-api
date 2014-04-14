package fr.xdcc.pi.model.service

import fr.xdcc.pi.model.bot.MongoBot
import org.jongo.Find
import org.jongo.FindOne
import org.jongo.MongoCollection
import spock.lang.Specification

@SuppressWarnings("GroovyAccessibility")
class MongoBotServiceTest extends Specification {

  MongoCollection mongoCollection
  MongoBotService mongoBotService
  MongoBot mongoBot

  def setup() {
    mongoCollection = Mock(MongoCollection)
    mongoBotService = new MongoBotService()
    mongoBotService.mongoBotCollection = mongoCollection
    mongoBot = Mock(MongoBot)
    mongoBot.name >> "botName"
  }

  def "list"() {
    given: "some other mocked MongoBots"
    MongoBot bot1 = Mock(MongoBot)
    bot1.name >> "bot1"
    MongoBot bot2 = Mock(MongoBot)
    bot2.name >> "bot2"
    MongoBot bot3 = Mock(MongoBot)
    bot3.name >> "bot3"

    and: "an Iterable of the mocked MongoBots"
    def iterable = [mongoBot, bot1, bot2, bot3] as Iterable<MongoBot>

    and: "the collection is mocked to return an Iterable of the mocked MongoBots"
    Find find = Mock(Find)
    find.as(MongoBot.class) >> iterable
    mongoCollection.find() >> find

    when: "retrieving the iterable of Mongobots"
    def result = mongoBotService.list()

    then: "the returned Iterable should match the previous one"
    result == iterable
  }

  def "insert"() {
    when: "insert method is called"
    mongoBotService.insert(mongoBot)

    then: "one insertion should be done on the collection"
    1 * mongoCollection.insert(mongoBot)
  }

  def "bot is retrieved from the DB"() {
    given: "the collection is mocked to return a bot"
    FindOne findOne = Mock(FindOne)
    mongoCollection.findOne("{name: #}", mongoBot.name) >> findOne
    findOne.as(MongoBot.class) >> mongoBot

    when: "retrieving the mocked bot from the DB"
    def result = mongoBotService.get(mongoBot.name)

    then: "the returned bot should match the mocked one"
    result == mongoBot
  }

  def "bot is not stored in the DB"() {
    given: "the collection is stubbed to return null"
    FindOne findOne = Mock(FindOne)
    mongoCollection.findOne("{name: #}", mongoBot.name) >> findOne
    findOne.as(MongoBot.class) >> null

    when: "retrieving the mocked bot from the DB"
    def result = mongoBotService.get(mongoBot.name)

    then: "the returned bot should be a newly instanciated one"
    result
    result instanceof MongoBot
    result != mongoBot
  }

  def "update"() {
    when: "update method is called"
    mongoBotService.update(mongoBot)

    then: "one update should be done on the collection"
    1 * mongoCollection.save(mongoBot)
  }

  def "count"() {
    when: "calling count"
    mongoBotService.count()

    then: "count is called one time on the collection"
    1 * mongoCollection.count()
  }
}
