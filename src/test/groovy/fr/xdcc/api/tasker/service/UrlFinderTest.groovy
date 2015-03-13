package fr.xdcc.api.tasker.service

import spock.lang.Specification

class UrlFinderTest extends Specification {

  UrlFinder urlFinder = new UrlFinder()

  def "retrieve the actual URL"(String botNickname, String expectedStringUrl) {
    expect:
    urlFinder.findBotUrl(botNickname) == expectedStringUrl

    where:
    botNickname << ["[SeriaL]Xdcc`Stargate`Universe", "[Darkside]`Z_Nation"]
    expectedStringUrl << [
        "http://listing.xdaysaysay.com/xdcc/serial_us,26/serialxdccstargateuniverse,189/",
        "http://listing.xdaysaysay.com/xdcc/serial_us,26/darksidez_nation,246/"
    ]
  }
}
