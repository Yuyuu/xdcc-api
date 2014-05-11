package xdcc.web.configuration;

import com.google.common.base.Strings;

import javax.inject.Inject;
import javax.inject.Named;

public class MongoDBConfiguration {
  @Inject
  @Named("db.host")
  public String host;

  @Inject
  @Named("db.name")
  public String name;

  @Inject
  @Named("db.user")
  public String user;

  @Inject
  @Named("db.password")
  public String password;

  @Inject
  @Named("db.port")
  public int port;

  public boolean withDBAuth() {
    return !Strings.isNullOrEmpty(user);
  }
}
