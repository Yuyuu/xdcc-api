package xdcc.web.resource;

import net.codestory.http.annotations.Get;

public class IndexResource {

  @Get("/")
  public String get() {
    return "xdcc-api Web Server";
  }
}
