package fr.xdcc.pi.web.resource;

import net.codestory.http.annotations.Get;

public class HomeResource {

  @Get("/")
  public String get() {
    return "xdcc-pi Web Server";
  }
}
