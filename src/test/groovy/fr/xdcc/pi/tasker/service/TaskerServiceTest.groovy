package fr.xdcc.pi.tasker.service

import fr.xdcc.pi.model.bot.Bot
import fr.xdcc.pi.model.bot.MongoBot
import fr.xdcc.pi.model.file.ConcreteFile
import fr.xdcc.pi.model.service.MongoBotService
import fr.xdcc.pi.tasker.parser.Parser
import spock.lang.Specification

@SuppressWarnings("GroovyAccessibility")
class TaskerServiceTest extends Specification {

  TaskerService taskerService
  MongoBotService mongoBotService
  Parser parser

  def setup() {
    taskerService = new TaskerService()
    mongoBotService = Mock(MongoBotService)
    parser = Mock(XdccListFileParser)

    taskerService.mongoBotService = mongoBotService
    taskerService.parser = parser
  }

  def "bot is updated when new files are found"() {
    given: "a mocked File"
    File file = new File("spock.txt")
    parser.parse(file) >> ["#1": "Ep1", "#2": "Ep2", "#3": "Ep3"]

    and: "a mocked MongoBot for MongoBotService to return"
    def botName = "bot"
    Bot bot = Mock(MongoBot)
    bot.fileSet >> new LinkedHashSet<ConcreteFile>()
    mongoBotService.get(botName) >> bot

    when: "calling updateAvailableFiles"
    taskerService.updateAvailableFiles(file, botName)

    then: "the set of files of the bot should be overriden"
    1 * bot.setFileSet(_ as LinkedHashSet)

    then: "the bot should be updated in the database"
    1 * mongoBotService.update(bot)
  }

  def "bot remain unchanged when no new file is found"() {
    given: "a mocked File"
    File file = new File("spock.txt")
    parser.parse(file) >> [:]

    and: "a mocked MongoBot for MongoBotService to return"
    def botName = "bot"
    Bot bot = Mock(MongoBot)
    bot.fileSet >> new LinkedHashSet<ConcreteFile>()
    mongoBotService.get(botName) >> bot

    when: "calling updateAvailableFiles"
    taskerService.updateAvailableFiles(file, botName)

    then: "the bot should be be updated"
    0 * bot.setFileSet(_ as LinkedHashSet)
    0 * mongoBotService.update(bot)
  }

  def "newly added bots are properly inserted in the database"() {
    given: "some mocked MongoBots"
    MongoBot bot1 = Mock(MongoBot)
    MongoBot bot2 = Mock(MongoBot)
    MongoBot bot3 = Mock(MongoBot)
    MongoBot newBot = Mock(MongoBot)

    bot1.name >> "FirstBot"
    bot2.name >> "SecondBot"
    bot3.name >> "ThirdBot"
    newBot.name >> "NewBot"

    and: "the first three stand as already saved in the database"
    def botNameList = [bot1.name, bot2.name, bot3.name, newBot.name]
    mongoBotService.getBotsIn(botNameList) >> ([bot1, bot2, bot3] as Iterable<MongoBot>)

    when: "calling updateAvailableBots"
    taskerService.updateAvailableBots(botNameList)

    then: "only the last mocked bot should be inserted in the database"
    1 * mongoBotService.insert(_ as MongoBot)
  }

  def "nothing happens when no new bot is found"() {
    given: "some mocked MongoBots"
    MongoBot bot1 = Mock(MongoBot)
    MongoBot bot2 = Mock(MongoBot)

    bot1.name >> "FirstBot"
    bot2.name >> "SecondBot"

    and: "all the mocked bots are already saved in the database"
    def botNameList = [bot1.name, bot2.name]
    mongoBotService.getBotsIn(botNameList) >> ([bot1, bot2] as Iterable<MongoBot>)

    when: "calling updateAvailableBots"
    taskerService.updateAvailableBots(botNameList)

    then: "no bot should be inserted in the database"
    0 * mongoBotService.insert(_ as MongoBot)
  }
}
