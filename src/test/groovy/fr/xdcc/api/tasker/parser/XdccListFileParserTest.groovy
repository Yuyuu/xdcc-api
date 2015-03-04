package fr.xdcc.api.tasker.parser

import spock.lang.Specification

class XdccListFileParserTest extends Specification {

  XdccListFileParser xdccListFileParser

  def setup() {
    xdccListFileParser = new XdccListFileParser()
  }

  def "parse - OK"() {
    given: "a mocked content to parse"
    String mockedFileContent =
        """
** To stop this listing, type "/MSG [SeriaL]Xdcc`TBBT XDCC STOP" **
** 158 packs **  5 of 5 slots open, Min: 10.0KB/s, Record: 2919.4KB/s
** Bandwidth Usage ** Current: 0.0KB/s, Cap: 500.0KB/s, Record: 500.8KB/s
** To request a file, type "/MSG [SeriaL]Xdcc`TBBT XDCC SEND x" **
** To request details, type "/MSG [SeriaL]Xdcc`TBBT XDCC INFO x" **
  #10   55x [175M] The.Big.Bang.Theory.S01E09.HDTV.XviD-XOR.avi
  #11   54x [174M] The.Big.Bang.Theory.S01E10.HDTV.XviD-LOL.avi
  #12   54x [175M] The.Big.Bang.Theory.S01E11.HDTV.XviD-FoV.avi
  #13   51x [175M] The.Big.Bang.Theory.S01E12.HDTV.XviD-FoV.avi
  #14   53x [175M] The.Big.Bang.Theory.S01E13.HDTV.[ XviD-FoV ].avi
  #15   53x [175M] The.Big.Bang.Theory.S01E14.HDTV.XviD-XOR.avi
  #16   50x [176M] The.Big.Bang.Theory.- S01E15 -.HDTV.XviD-FoV.avi
  #17   49x [175M] The.Big.Bang.Theory.S01E16.HDTV.XviD-XOR.avi
  #18   52x [175M] The.Big.Bang.Theory.S01E17.HDTV.XviD-LOL.avi
  #19   50x [249K] The.Big.Bang.Theory.S02.HDTV.XviD.SRT.VF.zip
  #20   55x [175M] The.Big.Bang.Theory.S02E01.REAL.PROPER.HDTV.XviD-NoTV.avi
** Fourni par PastisD@Otaku-IRC.fr **
Total Offered: 25GB  Total Transferred: 2.0TB
"""

    and: "a temporary file with the previous content"
    File tmpFile = File.createTempFile("XdccTmp", ".txt")
    BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile))
    bw.write(mockedFileContent)
    bw.close()

    when: "parsing the content of the file"
    def result = xdccListFileParser.parse(tmpFile)

    then: "the returned map should contain the following entries"
    result.size() == 11
    result.get("10") == "The.Big.Bang.Theory.S01E09.HDTV.XviD-XOR.avi"
    result.get("11") == "The.Big.Bang.Theory.S01E10.HDTV.XviD-LOL.avi"
    result.get("12") == "The.Big.Bang.Theory.S01E11.HDTV.XviD-FoV.avi"
    result.get("13") == "The.Big.Bang.Theory.S01E12.HDTV.XviD-FoV.avi"
    result.get("14") == "The.Big.Bang.Theory.S01E13.HDTV.[ XviD-FoV ].avi"
    result.get("15") == "The.Big.Bang.Theory.S01E14.HDTV.XviD-XOR.avi"
    result.get("16") == "The.Big.Bang.Theory.- S01E15 -.HDTV.XviD-FoV.avi"
    result.get("17") == "The.Big.Bang.Theory.S01E16.HDTV.XviD-XOR.avi"
    result.get("18") == "The.Big.Bang.Theory.S01E17.HDTV.XviD-LOL.avi"
    result.get("19") == "The.Big.Bang.Theory.S02.HDTV.XviD.SRT.VF.zip"
    result.get("20") == "The.Big.Bang.Theory.S02E01.REAL.PROPER.HDTV.XviD-NoTV.avi"
  }

  def "parse - Malformed content"() {
    given: "a mocked malformed content to parse"
    String mockedFileContent =
        """
  #10   55x [175M] [175M] [175M] The.Big.Bang.Theory.S01E09.HDTV.XviD-XOR.avi
"""

    and: "a temporary file with the previous content"
    File tmpFile = File.createTempFile("XdccTmp", ".txt")
    BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile))
    bw.write(mockedFileContent)
    bw.close()

    when: "parsing the content of the file"
    xdccListFileParser.parse(tmpFile)

    then: "an AssertionError should be thrown"
    thrown(AssertionError)
  }

  def "parse - Malformed pack Id"() {
    given: "a mocked malformed content to parse"
    String mockedFileContent =
        """
  10   55x [175M] The.Big.Bang.Theory.S01E09.HDTV.XviD-XOR.avi
"""

    and: "a temporary file with the previous content"
    File tmpFile = File.createTempFile("XdccTmp", ".txt")
    BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile))
    bw.write(mockedFileContent)
    bw.close()

    when: "parsing the content of the file"
    def result = xdccListFileParser.parse(tmpFile)

    then: "the returned map should be empty but no exception should be thrown"
    notThrown(Exception)
    result.isEmpty()
  }
}
