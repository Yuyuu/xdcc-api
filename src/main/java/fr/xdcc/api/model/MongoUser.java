package fr.xdcc.api.model;

import org.bson.types.ObjectId;

/**
 * @author Vincent Tertre
 */
public class MongoUser {

  public static final String COLLECTION_NAME = "view_mongouser";

  @SuppressWarnings("UnusedDeclaration")
  private ObjectId _id;
  private String login;
  private String password;

  // Required to instanciate object with Jackson
  @SuppressWarnings("UnusedDeclaration")
  private MongoUser() {}

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public String toString() {
    return "MongoUser{" +
        "login='" + login + '\'' +
        ", password='" + password + '\'' +
        '}';
  }
}
