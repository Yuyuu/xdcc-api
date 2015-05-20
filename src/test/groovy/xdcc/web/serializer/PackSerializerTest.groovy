package xdcc.web.serializer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import groovy.json.JsonSlurper
import org.bson.types.ObjectId
import spock.lang.Specification
import xdcc.model.bot.Pack

class PackSerializerTest extends Specification {

  JsonSlurper slurper = new JsonSlurper()
  ObjectMapper objectMapper

  void setup() {
    objectMapper = new ObjectMapper()
    objectMapper.registerModule(new SimpleModule().addSerializer(Pack.class, new PackSerializer()))
  }

  def "can serialize a pack"() {
    given:
    def botId = ObjectId.get()
    def pack = new Pack(1L, "title", botId)

    when:
    def result = slurper.parseText(objectMapper.writeValueAsString(pack))

    then:
    result.size() == 3
    result.botId == pack.botId().toHexString()
    result.title == pack.title()
    result.position == pack.position()
  }
}
