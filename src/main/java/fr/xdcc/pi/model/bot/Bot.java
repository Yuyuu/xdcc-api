package fr.xdcc.pi.model.bot;

import fr.xdcc.pi.model.file.ConcreteFile;
import org.bson.types.ObjectId;

import java.util.LinkedHashSet;

public interface Bot {
  ObjectId getId();
  String getName();
  LinkedHashSet<ConcreteFile> getFileSet();
  void setFileSet(LinkedHashSet<ConcreteFile> newFileSet);
}
