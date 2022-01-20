package base.parse;

import java.util.List;

/**
 * The Parser interface represents the ability to parse data of specific type from a file
 * @param <T> - The type of data to be parsed
 */
public interface Parser<T> {
    /**
     * Get all the records from the file
     * @return List of all records
     */
    public List<T> getRecords();
}
