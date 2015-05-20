package xdcc.web.serializer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import groovy.json.JsonSlurper
import spock.lang.Specification
import xdcc.model.bot.Bot
import xdcc.model.bot.Pack

@SuppressWarnings("GroovyAccessibility")
class BotSerializerTest extends Specification {

  JsonSlurper slurper = new JsonSlurper()
  ObjectMapper objectMapper

  void setup() {
    objectMapper = new ObjectMapper()
    objectMapper.registerModule(new SimpleModule().addSerializer(Bot.class, new BotSerializer()))
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
  }

  def "can serialize a bot"() {
    given:
    def bot = new Bot("kim")
    bot.packs = [new Pack(1L, "title", bot.id)] as Set

    when:
    def result = slurper.parseText(objectMapper.writeValueAsString(bot))

    then:
    result.size() == 3
    result.id == bot.id.toHexString()
    result.nickname == bot.nickname()
    result.packs.size() == 1
  }
}
