package fr.xdcc.pi.web.resource;

import org.bson.types.ObjectId;

public abstract class AbstractResource {
  public ObjectId parseObjectId(String id) {
    if (ObjectId.isValid(id)) {
      return new ObjectId(id);
    }

    // TODO : custom exception returned as JSON
    throw new IllegalArgumentException("Id is not a valid ObjectId");
  }
}
