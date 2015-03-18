package fr.vter.xdcc.tasker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UrlFinder {

  String findBotUrl(String botNickname) {
    String normalizedBotNickname = botNickname.replaceAll(NORMALIZE_REGEX, "").toLowerCase() + ",";
    String botUrl = WEBSITE_URL + "xdcc/serial_us,26/" + normalizedBotNickname;

    try {
      URL uri = new URL(WEBSITE_URL);
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
      LOG.debug("{}", exception.getMessage());
    }

    return null;
  }

  private static final String WEBSITE_URL = "http://listing.xdaysaysay.com/";
  private static final String NORMALIZE_REGEX = "[`\\[\\]]";
  private static final Logger LOG = LoggerFactory.getLogger(UrlFinder.class);
}
