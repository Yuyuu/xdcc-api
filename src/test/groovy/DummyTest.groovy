import spock.lang.Specification

class DummyTest extends Specification {

  def "it should be ok"() {
    expect:
    2  + 1 == 3
  }
}
