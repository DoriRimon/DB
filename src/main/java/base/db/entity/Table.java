package base.db.entity;

import java.util.List;

public abstract class Table {
    private String name;
    private List<String> columnsNames;
    private List<Class<?>> columnsTypes;

    public Table(String name, List<String> columnsNames, List<Class<?>> columnsTypes) {
        this.columnsNames = columnsNames;
        this.columnsTypes = columnsTypes;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public List<Class<?>> getColumnsTypes() {
        return columnsTypes;
    }

    public List<String> getColumnsNames() {
        return columnsNames;
    }

    public Class<?> getType(String column) {
        int index = this.columnsNames.indexOf(column);
        return this.columnsTypes.get(index);
    }
}
