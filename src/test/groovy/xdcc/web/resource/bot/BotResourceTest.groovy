package xdcc.web.resource.bot

import fr.vter.xdcc.infrastructure.bus.ExecutionResult
import fr.vter.xdcc.search.SearchBus
import spock.lang.Specification
import xdcc.model.bot.Bot
import xdcc.search.bot.BotDetailsSearch

class BotResourceTest extends Specification {

  SearchBus searchBus = Mock(SearchBus)
  BotResource botResource = new BotResource(searchBus)

  def "can return a bot"() {
    given:
    def bot = Mock(Bot)
    searchBus.sendAndWaitResponse(_ as BotDetailsSearch) >> ExecutionResult.success(bot)

    when:
    def result = botResource.show("id")

    then:
    result == bot
  }
}
