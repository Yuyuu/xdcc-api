package xdcc.model.bot;

import com.google.common.base.MoreObjects;
import org.bson.types.ObjectId;

public class Pack {

  // Required for mongolink
  @SuppressWarnings("unused")
  protected Pack() {}

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("position", position)
        .add("title", title)
        .toString();
  }

  private long position;
  private String title;
  private ObjectId botId;
}
