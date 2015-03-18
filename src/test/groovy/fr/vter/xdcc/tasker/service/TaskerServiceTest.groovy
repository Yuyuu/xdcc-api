package fr.vter.xdcc.tasker.service

import com.google.common.collect.Sets
import fr.vter.xdcc.model.Bot
import fr.vter.xdcc.model.MongoBot
import fr.vter.xdcc.model.ConcreteFile
import fr.vter.xdcc.infrastructure.persistence.mongo.MongoBotService
import fr.vter.xdcc.tasker.parser.XdccListFileParser
import fr.vter.xdcc.tasker.parser.XdccWebsiteParser
import spock.lang.Specification

@SuppressWarnings("GroovyAccessibility")
class TaskerServiceTest extends Specification {

  TaskerService taskerService
  MongoBotService mongoBotService
  XdccListFileParser xdccListFileParser
  XdccWebsiteParser xdccWebsiteParser
  UrlFinder urlFinder

  def setup() {
    mongoBotService = Mock(MongoBotService)
    xdccListFileParser = Mock(XdccListFileParser)
    xdccWebsiteParser = Mock(XdccWebsiteParser)
    urlFinder = Mock(UrlFinder)
    taskerService = new TaskerService(mongoBotService)

    taskerService.xdccListFileParser = xdccListFileParser
    taskerService.xdccWebsiteParser = xdccWebsiteParser
    taskerService.urlFinder = urlFinder
  }

  def "bot is updated when new files are found"() {
    given: "a mocked File"
    File file = new File("spock.txt")
    xdccListFileParser.parse(file) >> [1L: "Ep1", 2L: "Ep2", 3L: "Ep3"]

    and: "a mocked MongoBot for MongoBotService to return"
    def botName = "bot"
    Bot bot = Mock(MongoBot)
    bot.fileSet >> new HashSet<ConcreteFile>()
    mongoBotService.findByName(botName) >> bot

    when: "calling updateAvailableFiles"
    taskerService.updateAvailableFiles(file, botName)

    then: "the set of files and lastUpdated sate of the bot should be overridden"
    1 * bot.setFileSet(_ as HashSet)
    1 * bot.setLastUpdated(_ as Date)

    then: "the bot should be updated in the database"
    1 * mongoBotService.update(bot)
  }

  def "bot remain unchanged when no new file is found"() {
    given: "a mocked File"
    File file = new File("spock.txt")
    xdccListFileParser.parse(file) >> [1L: "Ep1", 2L: "Ep2", 3L: "Ep3"]

    and: "a mocked MongoBot for MongoBotService to return"
    def botName = "bot"
    Bot bot = Mock(MongoBot)
    bot.fileSet >> Sets.newHashSet(new ConcreteFile(3L, "Ep3"), new ConcreteFile(1L, "Ep1"), new ConcreteFile(2L, "Ep2"))
    mongoBotService.findByName(botName) >> bot

    when: "calling updateAvailableFiles"
    taskerService.updateAvailableFiles(file, botName)

    then: "the bot should not be updated"
    0 * bot.setFileSet(_ as HashSet)
  }

  def "bot is updated when new files are found (Website)"() {
    given: "a mocked MongoBot for MongoBotService to return"
    def botName = "bot"
    Bot bot = Mock(MongoBot)
    bot.fileSet >> new HashSet<ConcreteFile>()
    mongoBotService.findByName(botName) >> bot

    and:
    urlFinder.findBotUrl(botName) >> "http://url.com"
    xdccWebsiteParser.parse(_ as InputStream) >> [1L: "Ep1", 2L: "Ep2", 3L: "Ep3"]

    when: "calling updateAvailableFiles"
    taskerService.updateAvailableFiles(botName)

    then:
    1 * bot.setUrl("http://url.com")

    then: "the set of files and lastUpdated sate of the bot should be overridden"
    1 * bot.setFileSet(_ as HashSet)
    1 * bot.setLastUpdated(_ as Date)

    then: "the bot should be updated in the database"
    1 * mongoBotService.update(bot)
  }

  def "bot remain unchanged when no new file is found (Website)"() {
    given: "a mocked MongoBot for MongoBotService to return"
    def botName = "bot"
    Bot bot = Mock(MongoBot)
    bot.fileSet >> Sets.newHashSet(new ConcreteFile(3L, "Ep3"), new ConcreteFile(2L, "Ep2"), new ConcreteFile(1L, "Ep1"))
    mongoBotService.findByName(botName) >> bot

    and:
    urlFinder.findBotUrl(botName) >> "http://url.com"
    xdccWebsiteParser.parse(_ as InputStream) >> [2L: "Ep2", 1L: "Ep1", 3L: "Ep3"]

    when: "calling updateAvailableFiles"
    taskerService.updateAvailableFiles(botName)

    then: "the bot should not be updated"
    0 * bot.setFileSet(_ as HashSet)
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
    xdccListFileParser.parse(file) >> [:]

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
