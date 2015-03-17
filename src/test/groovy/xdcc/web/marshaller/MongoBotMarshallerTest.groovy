package xdcc.web.marshaller

import fr.xdcc.api.model.ConcreteFile
import fr.xdcc.api.model.MongoBot
import org.bson.types.ObjectId
import spock.lang.Specification

class MongoBotMarshallerTest extends Specification {

  MongoBotMarshaller mongoBotMarshaller

  def setup() {
    mongoBotMarshaller = new MongoBotMarshaller()
  }

  def "can be marshalled in full format"() {
    given: "a file set"
    LinkedHashSet<ConcreteFile> fileSet = new LinkedHashSet<>()
    fileSet.add(new ConcreteFile(1L, "Pack1"))
    fileSet.add(new ConcreteFile(2L, "Pack2"))

    and: "a mocked bot"
    MongoBot bot = Mock(MongoBot)
    ObjectId id = new ObjectId()
    bot.id >> id
    bot.name >> "bot"
    bot.lastChecked >> new Date()
    bot.lastUpdated >> new Date()
    bot.fileSet >> fileSet

    when:
    def result = mongoBotMarshaller.marshall(bot, Format.FULL)

    then: "the returned map should contain the following elements"
    result.size() == 6
    result.id == bot.id.toStringMongod()
    result.name == bot.name
    result.fileCount == 2
    result.lastChecked
    result.lastUpdated
    result.fileSet == fileSet
  }

  def "can be marshalled in short format"() {
    given:
    MongoBot bot = Mock(MongoBot)
    ObjectId id = new ObjectId()
    bot.id >> id
    bot.name >> "bot"
    bot.lastChecked >> new Date()
    bot.lastUpdated >> new Date()
    bot.fileSet >> []

    when:
    def result = mongoBotMarshaller.marshall(bot, Format.SHORT)

    then: "the returned map should contain the following elements"
    result.size() == 5
    result.id == bot.id.toStringMongod()
    result.name == bot.name
    result.fileCount == 0
    result.lastChecked
    result.lastUpdated
  }
}
