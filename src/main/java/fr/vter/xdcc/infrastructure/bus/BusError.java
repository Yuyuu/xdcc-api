package fr.vter.xdcc.infrastructure.bus;

public class BusError extends RuntimeException {

  public BusError(Throwable cause) {
    super(cause);
  }

  public BusError(String message, Throwable cause) {
    super(message, cause);
  }

  public BusError(String message) {
    super(message);
  }
}
