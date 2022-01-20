package base.parse;

import base.config.Configurations;
import org.apache.commons.csv.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * The CSVParser parses a CSV file
 */
public class CSVParser implements Parser<CSVRecord> {
    private String filePath;
    private org.apache.commons.csv.CSVParser parser;

    /**
     * Initializes CSV file to given file path
     * @param filePath - The file path
     * @throws IOException - If the reading of the file failed
     */
    public CSVParser(String filePath) throws IOException {
        this.parser = new org.apache.commons.csv.CSVParser(new FileReader(filePath), CSVFormat.RFC4180);
        this.filePath = filePath;
    }

    /**
     * Gets all the record in the file
     * @return - A list of all CSVRecords in the file, or null if parsing failed
     */
    @Override
    public List<CSVRecord> getRecords() {
        try {
            this.parser = new org.apache.commons.csv.CSVParser(new FileReader(filePath), CSVFormat.RFC4180);
            return this.parser.getRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
