package fr.xdcc.api.model;

import org.bson.types.ObjectId;

public class MissingMongoBotException extends RuntimeException {

  private ObjectId mongoBotId;

  public MissingMongoBotException(ObjectId mongoBotId) {
    super("The bot " + mongoBotId.toStringMongod() + " does not exist");
    this.mongoBotId = mongoBotId;
  }

  public ObjectId getMongoBotId() {
    return mongoBotId;
  }
}
