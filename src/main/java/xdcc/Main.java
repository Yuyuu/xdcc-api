package xdcc;

import fr.vter.xdcc.web.fluent.Server;
import xdcc.web.XdccApplication;

import java.util.Optional;

public class Main {

  public static void main(String[] args) throws Exception {
    new Server(new XdccApplication()).start(port());
  }

  private static int port() {
    final Optional<String> port = Optional.ofNullable(System.getenv("PORT"));
    return Integer.parseInt(port.orElse("8080"));
  }
}
