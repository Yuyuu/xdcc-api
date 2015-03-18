package fr.vter.xdcc.tasker.parser;

import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class XdccWebsiteParser {

  public Map<Long, String> parse(InputStream stream) throws IOException {
    Map<Long, String> map = Maps.newHashMap();
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

    String inputLine;
    while ((inputLine = reader.readLine()) != null) {
      if (inputLine.contains("Pack #")) {
        long packId = Long.parseLong(inputLine.substring(inputLine.indexOf("#") + 1, inputLine.indexOf("</td>")));
        inputLine = reader.readLine();
        String packName = inputLine.substring(inputLine.indexOf("title=\"") + 7, inputLine.indexOf("\">"));

        map.put(packId, packName);
      }
    }
    reader.close();

    return map;
  }
}
