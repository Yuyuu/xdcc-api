package fr.xdcc.api.tasker.parser;

public class PackEntry {

  private String id;
  private String title;

  public PackEntry(String id, String title) {
    this.id = id;
    this.title = title;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }
}
