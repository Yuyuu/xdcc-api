package fr.xdcc.pi.web.marshaller;

public enum Format {
  SHORT("short"),
  FULL("full");

  private final String value;

  Format(String value) {
    this.value = value;
  }


  public String value() {
    return value;
  }

  public static Format parseValue(String value) {
    for (Format format : Format.values()) {
      if (format.value.equals(value)) {
        return format;
      }
    }
    throw new IllegalArgumentException("'" + value + "' is not a valid format");
  }
}
