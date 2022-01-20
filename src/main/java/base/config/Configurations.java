package base.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * The Configurations class handles fetches from the config file
 */
public class Configurations {
    private Properties properties;

    /**
     * Initialize properties file to the config file's path
     * @param configFilePath - The path to the properties file
     */
    public Configurations(String configFilePath) {
        this.properties = new Properties();
        try {
            properties.load(new FileReader(configFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a property value of given key
     * @param key - The property's key
     * @return The related value
     */
    public String get(String key) {
        return this.properties.getProperty(key);
    }
}
