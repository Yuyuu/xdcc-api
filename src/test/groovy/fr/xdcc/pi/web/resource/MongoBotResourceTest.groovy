package fr.xdcc.pi.web.resource

import fr.xdcc.pi.model.bot.MongoBot
import fr.xdcc.pi.model.file.ConcreteFile
import fr.xdcc.pi.model.service.MongoBotService
import org.bson.types.ObjectId
import spock.lang.Specification

@SuppressWarnings("GroovyAccessibility")
class MongoBotResourceTest extends Specification {

  MongoBotResource mongoBotResource
  MongoBotService mongoBotService

  def setup() {
    mongoBotResource = new MongoBotResource()
    mongoBotService = Mock(MongoBotService)

    mongoBotResource.mongoBotService = mongoBotService
  }

  def "get"() {
    given: "some mocked bots"
    MongoBot bot1 = Mock(MongoBot)
    ObjectId id1 = new ObjectId()
    bot1.id >> id1
    bot1.name >> "bot1"
    ConcreteFile concreteFile = Mock(ConcreteFile)
    bot1.fileSet >> [concreteFile]

    MongoBot bot2 = Mock(MongoBot)
    ObjectId id2 = new ObjectId()
    bot2.id >> id2
    bot2.name >> "bot2"
    bot2.fileSet >> []

    def botList = [bot1, bot2]

    and: "list method of MongoBotService returns a list of the previous MongoBots"
    mongoBotService.list() >> botList

    when: "list method is called"
    def result = mongoBotResource.list()

    then: "the returned list should contain the following elements"
    result.size() == 2
    result.get(0).id == bot1.id.toStringMongod()
    result.get(0).name == bot1.name
    result.get(0).fileSet == bot1.fileSet
    result.get(1).id == bot2.id.toStringMongod()
    result.get(1).name == bot2.name
    result.get(1).fileSet == bot2.fileSet
  }

  def "show"() {
    given: "a file set"
    LinkedHashSet<ConcreteFile> fileSet = new LinkedHashSet<>()
    fileSet.add(new ConcreteFile("#1", "Pack1"))
    fileSet.add(new ConcreteFile("#2", "Pack2"))

    and: "a mocked bot"
    MongoBot bot = Mock(MongoBot)
    ObjectId id = new ObjectId()
    bot.id >> id
    bot.name >> "bot"
    bot.fileSet >> fileSet

    and: "get method of MongoBotService returns the previous mocked bot"
    mongoBotService.get(bot.id) >> bot

    when: "show method is called"
    def result = mongoBotResource.show(bot.id.toStringMongod())

    then: "the returned map should contan the following elements"
    result.id == bot.id.toStringMongod()
    result.name == bot.name
    result.fileSet == fileSet
  }
}
