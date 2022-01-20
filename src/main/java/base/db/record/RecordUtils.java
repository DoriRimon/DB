package base.db.record;

import base.db.record.Record;

import java.util.StringJoiner;

public class RecordUtils {
    public static String recordToCsv(Record record) {
        StringJoiner joiner = new StringJoiner(",");
        record.getValues().stream().map(Object::toString).forEach(joiner::add);
        return joiner.toString();
    }
}
