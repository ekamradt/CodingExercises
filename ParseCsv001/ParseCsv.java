import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*

We receive CSV's from out data pater that often have errors in them that can break downstream
 processes. As the first step in our process we need to prepare these CSV's for our data pipeline
 with the following rules:

- Discard blank rows and log the number of blank rows
- Discard rows with duplicate ids and log the number duplicates
- If multiple columns with the same name are encountered, we need to include the first one
    and drop any other columns and log the number of duplicate columns
- If non ascii characters are encountered, we want to strip them out but otherwise retain the row,
    and log the number of rows with invalid ascii characters.

The output should be a cleaned csv file ready for processing and a log of errors encountered along with
    your solution. Your solution can use any programming language of your choosing, but should be flexible.
    Meaning that you should be able to run it with any input file.

 */
public class ParseCsv {

    private List<String> lineList;
    private int blankLineCount = 0;
    private int dupIdCount = 0;
    private int dupNameCount = 0;
    private int badAsciiCount = 0;
    private int endingLineCount = 0;

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: java ParseCsv <filename> ... Need one input argument.");
        }

        final ParseCsv parseCsv = new ParseCsv(args[0]);
        parseCsv.run();
    }

    public ParseCsv(String filename) {
        lineList = fileIntoList(filename);
        if (lineList == null || lineList.size() < 2) {
            throw new IllegalArgumentException("Usage: java ParseCsv <filename> ... No data in file.");
        }
    }

    /**
     * Read a file into a list of Stirings
     */
    public List<String> fileIntoList(String filename) {
        try {
            Charset charset = StandardCharsets.ISO_8859_1;
            return Files.readAllLines(Paths.get(filename), charset);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void run() {
        // Ignore header line
        final Set<String> names = new HashSet<>();
        final Set<String> ids = new HashSet<>();
        final List<CsvLine> pojoList = new ArrayList<>();

        // Send header line to output
        out(this.lineList.get(0));
        endingLineCount++;

        for (int i = 1; i < lineList.size(); i++) {
            final String line = lineList.get(i);
            CsvLine csvLine = new CsvLine(line);

            if (checkForBlankLine(csvLine))
                continue;

            if (checkForDupName(names, csvLine))
                continue;

            if (checkforDupId(ids, csvLine))
                continue;

            if (csvLine.isBadAscii()) {
                badAsciiCount++;
            }

            out(csvLine.getLine());
            endingLineCount++;
        }
        displayStats();
    }

    private void displayStats() {
        errout("------------------------------");
        errout("--- Stats");
        errout("------------------------------");
        errout(String.format("Starting line count     : %d", lineList.size()));
        errout(String.format("Ending line count       : %d", endingLineCount));
        errout(String.format("Blank Line count        : %d", blankLineCount));
        errout(String.format("Duplicate Id count      : %d", dupIdCount));
        errout(String.format("Duplicate Name count    : %d", dupNameCount));
        errout(String.format("Bad Ascii in Line count : %d", badAsciiCount));

    }

    private void out(String s) {
        System.out.println(s);
    }

    private void errout(String s) {
        System.err.println(s);
    }

    private boolean checkForBlankLine(CsvLine csvLine) {
        if (csvLine.isBlank()) {
            blankLineCount++;
            return true;
        }
        return false;
    }

    private boolean checkforDupId(Set<String> ids, CsvLine csvLine) {
        final String id = csvLine.getId();
        if (ids.contains(id)) {
            dupIdCount++;
            return true;
        }
        ids.add(id);
        return false;
    }

    private boolean checkForDupName(Set<String> names, CsvLine csvLine) {
        final String nameKey = csvLine.nameKey();
        if (names.contains(nameKey)) {
            dupNameCount++;
            return true;
        }
        names.add(nameKey);
        return false;
    }

    public static class CsvLine {
        // "id","first","last","address","city","state","zip","address"
        private boolean haveData = false;
        private boolean badData = false;
        private boolean badAscii = false;

        private String id;
        private String first;
        private String last;
        private String address;
        private String city;
        private String state;
        private String zip;
        private String address2;

        public CsvLine(String line) {
            if (line != null) {
                final String tmpLine = line.trim();
                if (tmpLine.length() > 0) {
                    importDataLine(tmpLine);
                }
            }
        }

        private void importDataLine(String line) {
            try {
                String[] ary = line.split(",");
                int i = 0;
                id = parseInput(ary[i++]);
                first = parseInput(ary[i++]);
                last = parseInput(ary[i++]);
                address = parseInput(ary[i++]);
                city = parseInput(ary[i++]);
                state = parseInput(ary[i++]);
                zip = parseInput(ary[i++]);
                address2 = parseInput(ary[i]);
                haveData = true;
            } catch (Exception e) {
                badData = true;
            }
        }

        public String nameKey() {
            return String.format("%s-%s", first, last);
        }

        public boolean isBlank() {
            return !haveData && !badData;
        }

        public boolean isBad() {
            return badData;
        }

        public boolean isBadAscii() {
            return badAscii;
        }

        public String getId() {
            return id;
        }

        public String getLine() {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s", id, first, last, address, city, state, zip, address2);
        }

        private String parseInput(String s) {
            if (isPureAscii(s)) {
                return s;
            }
            badAscii = true;
            // Strip off ascii chars
            return s.replaceAll("[^\\p{ASCII}]", "");
        }

        private boolean isPureAscii(String v) {
            return StandardCharsets.US_ASCII.newEncoder().canEncode(v);
            // or "ISO-8859-1" for ISO Latin 1
            // or StandardCharsets.US_ASCII with JDK1.7+
        }
    }
}