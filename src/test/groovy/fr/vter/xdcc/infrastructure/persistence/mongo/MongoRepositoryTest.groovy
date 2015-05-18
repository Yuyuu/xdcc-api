package fr.vter.xdcc.infrastructure.persistence.mongo

import org.bson.types.ObjectId
import org.junit.Rule
import spock.lang.Specification

class MongoRepositoryTest extends Specification {

  @Rule
  WithMongoLink mongoLink = WithMongoLink.withPackage("fr.vter.xdcc.infrastructure.persistence.mongo.mapping")

  FakeEntityRepository repository

  def setup() {
    repository = new FakeEntityRepository(mongoLink.currentSession())
  }

  def "can get an entity"() {
    given:
    ObjectId id = ObjectId.get()
    mongoLink.collection("fakeentity") << [_id:id]

    when:
    def entity = repository.get(id)

    then:
    entity.id == id
  }
}
