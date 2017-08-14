package fr.scor.solem.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Rachid
 *
 */
public class Config {

    private Properties config;

    public Properties getConfig() {
        if (config == null) {
            config = new Properties();
            try {
        		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.properties");
                   config.load(inputStream);
               } catch (IOException ioe) {
                   ioe.printStackTrace();
            }

        }
        return config;
    }

}
