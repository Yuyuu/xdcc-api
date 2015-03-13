package fr.xdcc.api.model;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.LinkedHashSet;

public interface Bot {
  ObjectId getId();
  String getName();
  String getUrl();
  LinkedHashSet<ConcreteFile> getFileSet();
  Date getLastChecked();
  Date getLastUpdated();
  void setUrl(String url);
  void setFileSet(LinkedHashSet<ConcreteFile> newFileSet);
  void setLastChecked(Date time);
  void setLastUpdated(Date time);
}
