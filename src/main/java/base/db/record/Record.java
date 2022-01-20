package base.db.record;

import java.util.List;

public class Record {
    private List<Object> values;

    public Record(List<Object> values) {
        this.values = values;
    }

    public List<Object> getValues() {
        return this.values;
    }

    @Override
    public String toString() {
        return this.values.toString();
    }
}
