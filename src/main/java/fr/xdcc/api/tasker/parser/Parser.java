package fr.xdcc.api.tasker.parser;

import java.io.File;
import java.util.Map;

public interface Parser {
  /**
   * Parses the available packs from the file sent a the bot
   * @param file the file containing the list of available packs
   * @return a map which entries are the id and the name of the packs
   */
  Map<String, String> parse(File file);
}
