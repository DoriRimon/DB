package base.db.entity;

import base.config.Configurations;
import base.db.entity.Table;
import base.db.record.Record;
import base.host.fileHost.FileHost;
import base.parse.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DTable extends Table {
    private String name;
    private FileHost fileHost;

    public DTable(String name, String tableType, List<String> columnsNames, List<Class<?>> columnsTypes) throws IOException {
        super(name, columnsNames, columnsTypes);
        Configurations configurations = new Configurations("src/main/resources/config.properties");
        this.fileHost = new FileHost("dbDirPath", name, tableType, configurations, getColumnsNames(),
                getColumnsTypes());
    }

    public boolean deleteTable() {
        return this.fileHost.delete();
    }

    public void write(String record) throws IOException {
        this.fileHost.write(record);
    }

    public List<Record> getRecords() throws IOException {
        List<CSVRecord> csvRecords = this.fileHost.getRecords();
        List<Record> records = new ArrayList<>();

        for (CSVRecord csvRecord : csvRecords) {
            if (csvRecord.size() != this.getColumnsTypes().size()) {
                // TODO - shouldn't happen
            } else {
                List<Object> values = new ArrayList<>();
                for (int i = 0; i < csvRecord.size(); i++) {
                    Class<?> type = this.getColumnsTypes().get(i);
                    String value = csvRecord.get(i);

                    if (type.equals(Integer.class)) {
                        values.add(Integer.parseInt(value));
                    } else if (type.equals(Date.class)) {
                        values.add(Date.parse(value));
                    } else {
                        values.add(value);
                    }
                    // values.add(this.getColumnsTypes().get(i).cast(csvRecord.get(i)));
                }
                records.add(new Record(values));
            }
        }

        return records;
    }
}
