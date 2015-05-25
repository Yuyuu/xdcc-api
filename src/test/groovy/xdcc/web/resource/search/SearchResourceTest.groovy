package xdcc.web.resource.search

import fr.vter.xdcc.infrastructure.bus.ExecutionResult
import fr.vter.xdcc.search.SearchBus
import spock.lang.Specification
import xdcc.model.bot.Bot
import xdcc.search.BotsWhichNicknameContainsSearch

class SearchResourceTest extends Specification {

  SearchBus searchBus = Mock(SearchBus)
  SearchResource searchResource = new SearchResource(searchBus)

  def "returns a list of bots"() {
    given:
    def bot = Mock(Bot)
    searchBus.sendAndWaitResponse(_ as BotsWhichNicknameContainsSearch) >> ExecutionResult.success([bot])

    when:
    def result = searchResource.search("hi")

    then:
    result == [bot] as Iterable
  }
}
