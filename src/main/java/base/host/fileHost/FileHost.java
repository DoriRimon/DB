package base.host.fileHost;

import base.config.Configurations;
import base.host.Host;
import base.parse.CSVParser;
import base.parse.Parser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.List;
import java.util.StringJoiner;

public class FileHost extends Host {
    private File file;
    private Parser<CSVRecord> parser;

    private Configurations configurations;
    private final String type = "csv";
    private String tableType;
    private String tableName;
    private String fileName;

    private String dirPath;
    private String path;

    private List<String> columnsNames;
    private List<Class<?>> columnsTypes;

    public FileHost(String dirPathKey, String tableName, String tableType, Configurations configurations,
                    List<String> columnsNames, List<Class<?>> columnsTypes) throws IOException {
        this.columnsNames = columnsNames;
        this.columnsTypes = columnsTypes;

        this.configurations = configurations;
        String tableRootName = configurations.get("dbName");
        String tableNameSplitter = configurations.get("tableNameSplitter");
        this.tableType = tableType;
        this.tableName = tableName;
        this.fileName = tableRootName + tableNameSplitter + this.tableType + tableNameSplitter + this.tableName;
        this.dirPath = configurations.get(dirPathKey);
        this.path = this.dirPath + "\\" + this.fileName + "." + this.type;

        this.file = new File(this.path);

        if (!this.file.exists()) {
            createCsvHeader();
        }

        this.parser = new CSVParser(this.path);
    }

    private void createCsvHeader() {
        /* Create csv header */
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < this.columnsNames.size(); i++) {
            joiner.add(this.columnsNames.get(i) + " " + this.columnsTypes.get(i).getName());
        }
        try {
            write(joiner.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String record) throws IOException {
        try (FileWriter fw = new FileWriter(this.file, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(record);
        } catch (IOException e) {
            // TODO
        }
    }

    public List<CSVRecord> getRecords() {
        List<CSVRecord> csvRecords = this.parser.getRecords();
        csvRecords.remove(0);
        return csvRecords;
    }

    public boolean delete() {
        return this.file.delete();
    }
}
