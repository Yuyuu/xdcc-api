package fr.xdcc.pi.tasker.parser;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XdccListFileParser implements Parser {

  private static final Logger LOG = LoggerFactory.getLogger(XdccListFileParser.class);
  // Searches for the id of the pack declaration
  private static final String PACK_ID_REGEX = "#\\d+";
  // Searches for the end of the size declaration
  private static final String SPLIT_REGEX = "\\d{1,3}[KMG]\\]\\s";

  private Pattern splitPattern;
  private Matcher matcher;
  private LinkedHashMap<String, String> packMap;

  public XdccListFileParser() {
    Pattern packPattern = Pattern.compile(PACK_ID_REGEX);
    splitPattern = Pattern.compile(SPLIT_REGEX);
    matcher = packPattern.matcher("");
  }

  /**
   * @inheritDoc
   */
  @Override
  public Map<String, String> parse(File file) {
    packMap = Maps.newLinkedHashMap();
    Path pathToFile = FileSystems.getDefault().getPath(file.getAbsolutePath());

    try {
      Files.lines(pathToFile).filter(
          line -> line.trim().startsWith("#")
      ).forEach(this::extractPackEntry);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return packMap;
  }

  private void extractPackEntry(String packLine) {
    String[] splitPart = splitPattern.split(packLine);
    assert splitPart.length == 2;

    try {
      matcher.reset(splitPart[0]);
      if (matcher.find()) {
        packMap.put(matcher.group(), splitPart[1]);
      } else {
        LOG.debug("No packId match for line: [{}]", packLine);
      }
    } catch (Exception e) {
      LOG.debug("[{}]: {}", packLine, e.getMessage());
    }
  }
}
