package xdcc.search

import fr.vter.xdcc.search.WithJongo
import org.bson.types.ObjectId
import org.junit.Rule
import spock.lang.Specification

class SearchBotsWhichNicknameContainsHandlerTest extends Specification {

  @Rule
  WithJongo jongo = new WithJongo()

  def "returns the bots which nickname contains a string"() {
    given:
    jongo.collection("bot") << [
        [_id: ObjectId.get(), nickname:"hello", packs:[]],
        [_id: ObjectId.get(), nickname:"hey", packs:[]],
        [_id: ObjectId.get(), nickname:"kim", packs:[]]
    ]

    when:
    def result = new SearchBotsWhichNicknameContainsHandler().execute(new BotsWhichNicknameContainsSearch("he"), jongo.jongo())

    then:
    result.size() == 2
    result.find { it.nickname() == "hello" } != null
    result.find { it.nickname() == "hey" } != null
  }
}
