package xdcc.web.resource;

import net.codestory.http.annotations.Get;

public class HomeResource {

  @Get("/")
  public String get() {
    return "xdcc-api Web Server";
  }
}
