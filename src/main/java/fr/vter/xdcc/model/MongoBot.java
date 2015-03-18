package fr.vter.xdcc.model;


import com.google.common.collect.Sets;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Set;

public class MongoBot implements Bot {

  public static final String COLLECTION_NAME = "view_mongobot";

  @SuppressWarnings("UnusedDeclaration")
  private ObjectId _id;
  private String name;
  private String url;
  private Set<ConcreteFile> fileSet = Sets.newLinkedHashSet();
  private Date lastChecked;
  private Date lastUpdated;

  // Required to instanciate object with Jackson
  @SuppressWarnings("UnusedDeclaration")
  private MongoBot() {}

  public MongoBot(String name) {
    this.name = name;
  }

  @Override
  public ObjectId getId() {
    return _id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getUrl() {
    return url;
  }

  @Override
  public Set<ConcreteFile> getFileSet() {
    return fileSet;
  }

  @Override
  public Date getLastChecked() {
    return lastChecked;
  }

  @Override
  public Date getLastUpdated() {
    return lastUpdated;
  }

  @Override
  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public void setFileSet(Set<ConcreteFile> newFileSet) {
    fileSet = newFileSet;
  }

  @Override
  public void setLastChecked(Date time) {
    lastChecked = time;
  }

  @Override
  public void setLastUpdated(Date time) {
    lastUpdated = time;
  }


  @Override
  public boolean equals(Object document) {
    if (this == document) return true;
    if (document == null || getClass() != document.getClass()) return false;

    MongoBot mongoBot = (MongoBot) document;

    if (!_id.equals(mongoBot._id)) return false;
    if (!name.equals(mongoBot.name)) return false;
    if (!fileSet.equals(mongoBot.fileSet)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = _id.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + fileSet.hashCode();
    return result;
  }
}
