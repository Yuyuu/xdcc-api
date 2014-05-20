package fr.xdcc.api.tasker.service

import fr.xdcc.api.model.Bot
import fr.xdcc.api.model.MongoBot
import fr.xdcc.api.model.ConcreteFile
import fr.xdcc.api.infrastructure.persistence.mongo.MongoBotService
import fr.xdcc.api.tasker.parser.Parser
import fr.xdcc.api.tasker.parser.XdccListFileParser
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
    mongoBotService.findByName(botName) >> bot

    when: "calling updateAvailableFiles"
    taskerService.updateAvailableFiles(file, botName)

    then: "the set of files and lastUpdated sate of the bot should be overridden"
    1 * bot.setFileSet(_ as LinkedHashSet)
    1 * bot.setLastUpdated(_ as Date)

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
    mongoBotService.findByName(botName) >> bot

    when: "calling updateAvailableFiles"
    taskerService.updateAvailableFiles(file, botName)

    then: "the bot should be be updated"
    0 * bot.setFileSet(_ as LinkedHashSet)
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

  def "lastChecked date is updated when a bot is being checked"() {
    given: "a mocked File"
    File file = new File("spock.txt")
    parser.parse(file) >> [:]

    and: "a mocked MongoBot for MongoBotService to return"
    def botName = "bot"
    def lastChecked = new Date()
    Bot bot = Mock(MongoBot)
    bot.lastChecked >> lastChecked
    mongoBotService.findByName(botName) >> bot

    when: "calling updateAvailableFiles"
    taskerService.updateAvailableFiles(file, botName)

    then:
    1 * bot.setLastChecked(_ as Date)
  }
}
