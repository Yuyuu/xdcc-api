package fr.vter.xdcc.model;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Set;

public interface Bot {
  ObjectId getId();
  String getName();
  String getUrl();
  Set<ConcreteFile> getFileSet();
  Date getLastChecked();
  Date getLastUpdated();
  void setUrl(String url);
  void setFileSet(Set<ConcreteFile> newFileSet);
  void setLastChecked(Date time);
  void setLastUpdated(Date time);
}
