package xdcc.search.bot

import fr.vter.xdcc.search.WithJongo
import org.bson.types.ObjectId
import org.junit.Rule
import spock.lang.Specification

@SuppressWarnings("GroovyAccessibility")
class SearchAllBotsHandlerTest extends Specification {

  @Rule
  WithJongo jongo = new WithJongo()

  def "can return all the bots"() {
    given:
    def bobId = ObjectId.get()
    def kimId = ObjectId.get()
    jongo.collection("bot") << [
        [_id:bobId, nickname:"bob", packs:[[position:6, title:"episode 6", botId:bobId]]],
        [_id:kimId, nickname:"kim", packs:[[position:45, title:"episode 45", botId:kimId]]]
    ]

    when:
    def results = new SearchAllBotsHandler().execute(new AllTheBotsSearch(), jongo.jongo())

    then:
    results.size() == 2
    def bobMetadata = results.first()
    bobMetadata.id == bobId
    bobMetadata.nickname == "bob"
  }
}
