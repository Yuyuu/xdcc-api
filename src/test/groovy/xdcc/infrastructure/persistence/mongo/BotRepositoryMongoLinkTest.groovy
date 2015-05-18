package xdcc.infrastructure.persistence.mongo

import fr.vter.xdcc.infrastructure.persistence.mongo.WithMongoLink
import org.bson.types.ObjectId
import org.junit.Rule
import spock.lang.Specification

@SuppressWarnings("GroovyAccessibility")
class BotRepositoryMongoLinkTest extends Specification {

  @Rule
  WithMongoLink mongoLink = WithMongoLink.withPackage("xdcc.infrastructure.persistence.mongo.mapping")

  BotRepositoryMongoLink repository

  def setup() {
    repository = new BotRepositoryMongoLink(mongoLink.currentSession())
  }

  def "can get a bot"() {
    given:
    def botId = ObjectId.get();
    def packId = ObjectId.get();
    mongoLink.collection("bot") << [_id:botId, nickname:"joe", packs:[[_id:packId, position:6, title:"episode 6", botId: botId]]]

    when:
    def bot = repository.get(botId)

    then:
    bot.id == botId
    bot.nickname == "joe"
    def pack = bot.packs().first()
    pack.position == 6
    pack.title == "episode 6"
  }
}
