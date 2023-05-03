package automation.core;


import java.io.FileInputStream;
import java.util.Properties;


public class PropertyReader {

    private static Properties properties = loadProperties();

    private static Properties loadProperties() {
        Properties properties1 = new Properties();

        try {
            properties1.load(new FileInputStream("config.properties"));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return properties1;
    }

    public static String getProperty(String propertyKey) {
        return properties.getProperty(propertyKey);
    }

}
