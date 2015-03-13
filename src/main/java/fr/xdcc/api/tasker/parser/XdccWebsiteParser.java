package fr.xdcc.api.tasker.parser;

import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

public class XdccWebsiteParser {

  public Map<String, String> parse(String botNickname) {
    Map<String, String> map = Maps.newLinkedHashMap();
    String botUrl = retrieveBotUrl(botNickname);

    try {
      URL url = new URL(botUrl);
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

      String inputLine;
      while ((inputLine = reader.readLine()) != null) {
        if (inputLine.contains("Pack #")) {
          // Get pack number
          String packId = inputLine.substring(inputLine.indexOf("#") + 1, inputLine.indexOf("</td>"));

          // Go to next line for pack name
          inputLine = reader.readLine();

          // Extract pack name
          String packName = inputLine.substring(inputLine.indexOf("title=\"") + 7, inputLine.indexOf("\">"));

          map.put(packId, packName);
        }
      }

      reader.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    return map;
  }

  private String retrieveBotUrl(String botNickname) {
    String normalizedBotNickname = botNickname.replaceAll("[`\\[\\]]", "").toLowerCase() + ",";
    String botUrl = "http://listing.xdaysaysay.com/xdcc/serial_us,26/" + normalizedBotNickname;

    try {
      URL uri = new URL("http://listing.xdaysaysay.com/");
      BufferedReader reader = new BufferedReader(new InputStreamReader(uri.openStream()));

      String inputLine;
      while ((inputLine = reader.readLine()) != null) {
        if (inputLine.contains(normalizedBotNickname)) {
          reader.close();
          botUrl += inputLine.substring(inputLine.indexOf("/\"") - 3, inputLine.indexOf("/\"")) + "/";
          return botUrl;
        }
      }

      reader.close();
    } catch (IOException exception) {
      exception.printStackTrace();
    }

    return null;
  }
}
