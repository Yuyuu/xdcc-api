package fr.xdcc.pi.web.resource;

public class InvalidObjectIdException extends RuntimeException {

  private String id;

  public InvalidObjectIdException(String id) {
    super("Id " + id + " is not a valid ObjectId");
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
