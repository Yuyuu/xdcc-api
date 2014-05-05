package fr.xdcc.api.model;


import com.google.common.collect.Sets;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.LinkedHashSet;

public class MongoBot implements Bot {

  public static final String COLLECTION_NAME = "mongobots";

  @SuppressWarnings("UnusedDeclaration")
  private ObjectId _id;
  private String name;
  private LinkedHashSet<ConcreteFile> fileSet = Sets.newLinkedHashSet();
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
  public LinkedHashSet<ConcreteFile> getFileSet() {
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
  public void setFileSet(LinkedHashSet<ConcreteFile> newFileSet) {
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
