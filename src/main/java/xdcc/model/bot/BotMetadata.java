package xdcc.model.bot;

import fr.vter.xdcc.model.EntityWithObjectId;
import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.Id;

public class BotMetadata implements EntityWithObjectId {

  protected BotMetadata() {}

  public BotMetadata(String nickname) {
    id = ObjectId.get();
    this.nickname = nickname;
  }

  @Override
  public ObjectId getId() {
    return id;
  }

  public String nickname() {
    return nickname;
  }

  @Id
  private ObjectId id;
  private String nickname;
}
