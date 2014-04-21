package fr.xdcc.pi.web.marshaller

import fr.xdcc.pi.model.ConcreteFile
import fr.xdcc.pi.model.MongoBot
import org.bson.types.ObjectId
import spock.lang.Specification

class MongoBotMarshallerTest extends Specification {

  MongoBotMarshaller mongoBotMarshaller

  def setup() {
    mongoBotMarshaller = new MongoBotMarshaller()
  }

  def "is marshalled properly"() {
    given: "a file set"
    LinkedHashSet<ConcreteFile> fileSet = new LinkedHashSet<>()
    fileSet.add(new ConcreteFile("#1", "Pack1"))
    fileSet.add(new ConcreteFile("#2", "Pack2"))

    and:
    MongoBot bot = Mock(MongoBot)
    ObjectId id = new ObjectId()
    bot.id >> id
    bot.name >> "bot"
    bot.fileSet >> fileSet

    when:
    def result = mongoBotMarshaller.marshall(bot)

    then: "the returned map should contain the following elements"
    result.size() == 3
    result.id == bot.id.toStringMongod()
    result.name == bot.name
    result.fileSet == fileSet
  }
}
