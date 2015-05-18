package xdcc.model.bot;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import fr.vter.xdcc.model.EntityWithObjectId;
import org.bson.types.ObjectId;

import java.util.Set;

public class Bot implements EntityWithObjectId {

  // Required for mongolink
  @SuppressWarnings("unused")
  protected Bot() {}

  @Override
  public ObjectId getId() {
    return _id;
  }

  public String nickname() {
    return nickname;
  }

  public Set<Pack> packs() {
    return packs;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", _id)
        .add("nickname", nickname)
        .toString();
  }

  private ObjectId _id;
  private String nickname;
  private Set<Pack> packs = Sets.newHashSet();
}
