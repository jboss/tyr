package org.xstefank.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.xstefank.model.Utils.JBOSS_CONFIG_DIR;
import static org.xstefank.model.Utils.CONFIG_FILE;

public class TyrProperties {

    private static final Properties properties =
            loadProperties(System.getProperty(JBOSS_CONFIG_DIR), CONFIG_FILE);

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    static Properties loadProperties(String dirName, String fileName) {
        Properties properties = new Properties();
        if (dirName != null && fileName != null) {
            File dir = new File(dirName);
            File fileProp = new File(dir, fileName);
            if (fileProp.exists()) {
                try (InputStream is = new FileInputStream(fileProp)) {
                    properties.load(is);
                } catch (IOException e) {
                    throw new IllegalArgumentException("There was a problem with reading properties", e);
                }
            }
        }
        return properties;
    }
}
