import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by andrewgorton on 04/12/2016.
 */
public class PropertiesLoader {
    Properties properties = null;

    public PropertiesLoader() {
        try {
            InputStream in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);
        } catch (IOException io) {
            System.out.println("Error loading properties.");
        }
    }

    public Properties getProperties() {
        return properties;
    }
}
