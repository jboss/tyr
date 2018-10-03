package org.xstefank.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class Utils {

    public static final String GITHUB_BASE = "https://api.github.com";
    public static final String CONFIG_FILE = "config.properties";
    public static final String TOKEN_PROPERTY = "github.oauth.token";
    public static final String TOKEN_ENV = "GITHUB_OAUTH_TOKEN";
    public static final String JBOSS_CONFIG_DIR = "jboss.server.config.dir";
    public static final String TEMPLATE_FORMAT_URL = "template.format.url";

    //PR payload
    public static final String PULL_REQUEST = "pull_request";
    public static final String BODY = "body";
    public static final String HEAD = "head";
    public static final String SHA = "sha";
    public static final String TITLE = "title";
    public static final String COMMITS = "commits";

    //Commit payload
    public static final String COMMIT = "commit";
    public static final String MESSAGE = "message";
    public static final String URL = "url";

    private static Properties tyrProperties = null;

    static void loadProperties(String dirName, String fileName) {
        tyrProperties = new Properties();
        if (dirName != null && fileName != null) {
            File dir = new File(dirName);
            File fileProp = new File(dir, fileName);
            if (fileProp.exists()) {
                try (InputStream is = new FileInputStream(fileProp)) {
                    tyrProperties.load(is);
                } catch (IOException e) {
                    throw new IllegalArgumentException("There was a problem with reading properties", e);
                }
            }
        }
    }

    public static String getTyrProperty(String key) {
        if (tyrProperties == null)
            loadProperties(System.getProperty(JBOSS_CONFIG_DIR), CONFIG_FILE);
        return tyrProperties.getProperty(key);
    }

    public static URL getFormatUrl() throws MalformedURLException {
        String target = System.getProperty(TEMPLATE_FORMAT_URL);
        if (target == null)
            target = getTyrProperty(TEMPLATE_FORMAT_URL);
        return target != null ? new URL(target) : null;
    }
}