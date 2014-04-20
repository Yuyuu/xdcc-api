package fr.xdcc.pi.web.resource

import fr.xdcc.pi.model.bot.MongoBot
import fr.xdcc.pi.model.file.ConcreteFile
import fr.xdcc.pi.model.service.MongoBotService
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
    MongoBot bot2 = Mock(MongoBot)

    bot1.name >> "bot1"
    bot2.name >> "bot2"

    def botList = [bot1, bot2]

    and: "list method of MongoBotService returns a list of the previous MongoBots"
    mongoBotService.list() >> botList

    when: "list method is called"
    def result = mongoBotResource.list()

    then: "the returned list should contain the name of the mocked MongoBots"
    result == [bot1.name, bot2.name]
  }

  def "show"() {
    given:
    LinkedHashSet<ConcreteFile> fileSet = new LinkedHashSet<>()
    fileSet.add(new ConcreteFile("#1", "Pack1"))
    fileSet.add(new ConcreteFile("#2", "Pack2"))

    and: "a mocked bot"
    MongoBot bot = Mock(MongoBot)
    bot.name >> "bot"
    bot.fileSet >> fileSet

    and: "findByName method of MongoBotService returns the previous mocked bot"
    mongoBotService.findByName(bot.name) >> bot

    when: "show method is called"
    def result = mongoBotResource.show(bot.name)

    then: "the returned set should match the following"
    result == fileSet
  }
}
