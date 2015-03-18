package fr.vter.xdcc.model;

public class ConcreteFile {

  long packId;
  String name;

  // Required to instanciate object with Jackson
  private ConcreteFile() {}

  public ConcreteFile(long packId, String name) {
    this.packId = packId;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public long getPackId() {
    return packId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ConcreteFile that = (ConcreteFile) o;

    if (packId != that.packId) return false;
    if (!name.equals(that.name)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (packId ^ (packId >>> 32));
    result = 31 * result + name.hashCode();
    return result;
  }
}
