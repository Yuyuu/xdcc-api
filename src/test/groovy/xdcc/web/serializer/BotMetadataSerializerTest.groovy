package xdcc.web.serializer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import groovy.json.JsonSlurper
import spock.lang.Specification
import xdcc.model.bot.BotMetadata

class BotMetadataSerializerTest extends Specification {

  JsonSlurper slurper = new JsonSlurper()
  ObjectMapper objectMapper

  void setup() {
    objectMapper = new ObjectMapper()
    objectMapper.registerModule(new SimpleModule().addSerializer(BotMetadata.class, new BotMetadataSerializer()))
  }

  def "can serialize the metadata"() {
    given:
    def botMetadata = new BotMetadata("kim")

    when:
    def result = slurper.parseText(objectMapper.writeValueAsString(botMetadata))

    then:
    result.size() == 2
    result.id == botMetadata.id.toHexString()
    result.nickname == botMetadata.nickname()
  }
}
