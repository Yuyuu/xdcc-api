package xdcc.web.resource;

import org.bson.types.ObjectId;

public abstract class AbstractResource {

  public ObjectId parseObjectId(String id) {
    if (!ObjectId.isValid(id)) {
      throw new InvalidObjectIdException(id);
    }

    return new ObjectId(id);
  }
}
