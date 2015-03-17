package fr.xdcc.api.tasker.parser;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XdccListFileParser {

  private static final Logger LOG = LoggerFactory.getLogger(XdccListFileParser.class);
  // Searches for the id of the pack declaration
  private static final String PACK_ID_REGEX = "#\\d+";
  // Searches for the end of the size declaration
  private static final String SPLIT_REGEX = "(?i)\\d{1,3}[KMG]\\]\\s";

  private Pattern splitPattern;
  private Matcher matcher;

  public XdccListFileParser() {
    Pattern packPattern = Pattern.compile(PACK_ID_REGEX);
    splitPattern = Pattern.compile(SPLIT_REGEX);
    matcher = packPattern.matcher("");
  }

  public Map<Long, String> parse(File file) {
    Map<Long, String> packMap = Maps.newHashMap();
    Path pathToFile = FileSystems.getDefault().getPath(file.getAbsolutePath());

    LOG.info("Parsing file: {}", file.getName());
    try {
      Files.lines(pathToFile).filter(
          line -> line.trim().startsWith("#")
      ).map(this::extractPackEntry).forEach(
          pack -> packMap.put(pack.getId(), pack.getTitle())
      );
    } catch (IOException e) {
      LOG.debug("Exception in method parse: {}", e.getMessage());
    }

    return packMap;
  }

  private PackEntry extractPackEntry(String packLine) {
    String[] splitPart = splitPattern.split(packLine);
    assert splitPart.length == 2;

    try {
      matcher.reset(splitPart[0]);
      if (matcher.find()) {
        // Remove #
        return new PackEntry(Long.parseLong(matcher.group().substring(1)), splitPart[1]);
      } else {
        LOG.debug("No packId match for line: [{}]", packLine);
      }
    } catch (Exception e) {
      LOG.debug("[{}]: {}", packLine, e.getMessage());
    }

    return null;
  }

  private class PackEntry {

    private long id;
    private String title;

    public PackEntry(long id, String title) {
      this.id = id;
      this.title = title;
    }

    public long getId() {
      return id;
    }

    public String getTitle() {
      return title;
    }
  }
}
