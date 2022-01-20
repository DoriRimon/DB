package base.db;

import base.db.entity.DTable;
import base.db.record.Record;
import base.db.record.RecordUtils;
import base.config.Configurations;
import base.parse.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DDB {
    private Configurations configurations;
    private Set<DTable> tables;
    private HashMap<String, DTable> indexes;
    private String name;

    public DDB() {
        this.tables = new HashSet<>();
        this.indexes = new HashMap<>();

        this.configurations = new Configurations("src/main/resources/config.properties");

        String dirPath = this.configurations.get("dbDirPath");
        this.name = configurations.get("dbName");

        File dir = new File(dirPath);
        File[] tables = dir.listFiles();
        if (tables != null) {
            for (File table : tables) {
                DTable tableObj = null;
                try {
                    tableObj = parseTable(table);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (tableObj != null) {
                    this.tables.add(tableObj);
                }
            }
        }
    }

    private DTable parseTable(File table) throws IOException, ClassNotFoundException {
        String fileName = table.getName();
        String[] parts = fileName.split(this.configurations.get("tableNameSplitter"));
        String dbName = parts[0];
        String tableType = parts[1];
        String tableName = parts[2];

        String[] tableNameParts = tableName.split("\\.");
        tableName = tableNameParts[0];

        if (!dbName.equals(this.name)) {
            return null;
        }

        CSVRecord header = new CSVParser(table.getPath()).getRecords().get(0);
        List<String> columnsNames = new ArrayList<>();
        List<Class<?>> columnsTypes = new ArrayList<>();
        for (int i = 0; i < header.size(); i++) {
            String col = header.get(i);
            String[] colParts = col.split(" ");
            String colName = colParts[0];
            Class<?> colType = Class.forName(colParts[1]);

            columnsNames.add(colName);
            columnsTypes.add(colType);
        }

        return new DTable(tableName, tableType, columnsNames, columnsTypes);
    }

    public boolean createTable(String name, List<String> columnsNames, List<Class<?>> columnsTypes) throws IOException {
        // TODO - validations: table with same name, columns matches
        if (getTable(name) != null || columnsNames.size() != columnsTypes.size()) {
            return false;
        }

        if (columnsTypes.stream().anyMatch(type -> !Arrays.asList(String.class, Integer.class, Date.class)
                .contains(type))) {
            return false;
        }

        DTable table = new DTable(name, "userTable", columnsNames, columnsTypes);
        this.tables.add(table);
        return true;
    }

    public boolean createIndex(String tableName, String columnName) throws IOException {
        DTable table = getTable(tableName);

        if (table == null) {
            return false;
        }

        List<String> columnsNames = new ArrayList<>(Arrays.asList(columnName, "hash"));
        List<Class<?>> columnsTypes = new ArrayList<>(Arrays.asList(table.getType(columnName), Integer.class));
        DTable index = new DTable(columnName, "index", columnsNames, columnsTypes);

        List<Record> records = table.getRecords();


        this.indexes.put(tableName, index);

        return true;
    }

    private DTable getTable(String tableName) {
        DTable table = null;
        for (DTable t : this.tables) {
            if (t.getName().equals(tableName)) {
                table = t;
            }
        }
        return table;
    }

    public void deleteTable(String name) {
        List<DTable> tables = this.tables.stream().filter(table -> table.getName().equals(name))
                .collect(Collectors.toList());
        tables.forEach(DTable::deleteTable);
        this.tables.removeAll(tables);
    }

    public boolean insert(String tableName, Record record) throws IOException {
        // TODO - validations (types, length)

        // validate

        // check if unique value exists

        // push
        DTable table = getTable(tableName);

        if (table == null) {
            return false;
        }

        table.write(RecordUtils.recordToCsv(record));
        return true;
    }

    public List<Record> select(String tableName, HashMap<String, Object> constraints) throws IOException {
        List<Record> records = getTable(tableName).getRecords();

        // TODO - filter by constraints

        return records;
    }
}
