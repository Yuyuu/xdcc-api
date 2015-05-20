package xdcc.search.bot

import fr.vter.xdcc.search.WithJongo
import org.bson.types.ObjectId
import org.junit.Rule
import spock.lang.Specification

@SuppressWarnings("GroovyAccessibility")
class SearchBotDetailsHandlerTest extends Specification {

  @Rule
  WithJongo jongo = new WithJongo()

  def "can return a bot and its properties"() {
    given:
    def kimId = ObjectId.get()
    jongo.collection("bot") << [_id:kimId, nickname:"kim", packs:[
        [position:45, title:"episode 45", botId:kimId],
        [position:23, title:"episode 23", botId:kimId],
        [position:1, title:"episode 1", botId:kimId],
    ]]

    when:
    def result = new SearchBotDetailsHandler().execute(new BotDetailsSearch(kimId.toString()), jongo.jongo())

    then:
    result.nickname == "kim"
    result.packs.size() == 3
    result.packs.every { it.botId == kimId }
    result.packs().find { it.position == 1 }.title == "episode 1"
  }

  def "throws an exception if the given id is not a valid ObjectId"() {
    when:
    new SearchBotDetailsHandler().execute(new BotDetailsSearch("hello"), jongo.jongo())

    then:
    thrown(IllegalArgumentException)
  }
}
