package fr.xdcc.api.web.fluent;

import net.codestory.http.WebServer;

public class Server {

  public Server(BaseApplication application) {
    webServer = new WebServer(application.routes());
  }

  public void start(int port) throws Exception {
    webServer.start(port);
  }

  private final WebServer webServer;
}
