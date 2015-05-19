package xdcc.web.resource.bot

import fr.vter.xdcc.infrastructure.bus.ExecutionResult
import fr.vter.xdcc.search.SearchBus
import spock.lang.Specification
import xdcc.model.bot.Bot
import xdcc.search.bot.AllTheBots

class BotsResourceTest extends Specification {

  SearchBus searchBus = Mock(SearchBus)
  BotsResource botsResource = new BotsResource(searchBus)

  def "returns all the bots"() {
    given:
    def bot = Mock(Bot)
    searchBus.sendAndWaitResponse(_ as AllTheBots) >> ExecutionResult.success([bot])

    when:
    def result = botsResource.list()

    then:
    result == [bot]
  }
}
