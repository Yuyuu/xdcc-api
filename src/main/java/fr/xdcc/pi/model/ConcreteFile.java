package fr.xdcc.pi.model;

public class ConcreteFile {

  String packId;
  String name;

  // Required to instanciate object with Jackson
  private ConcreteFile() {}

  public ConcreteFile(String packId, String name) {
    this.packId = packId;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getPackId() {
    return packId;
  }

  @Override
  public boolean equals(Object document) {
    if (this == document) return true;
    if (document == null || getClass() != document.getClass()) return false;

    ConcreteFile that = (ConcreteFile) document;

    if (!name.equals(that.name)) return false;
    if (!packId.equals(that.packId)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + packId.hashCode();
    return result;
  }
}
