package org.xstefank.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

	public static final String GITHUB_BASE = "https://api.github.com";
	public static final String CONFIG_FILE = "config.properties";
	public static final String TOKEN_PROPERTY = "github.oauth.token";
	public static final String TOKEN_ENV = "GITHUB_OAUTH_TOKEN";
	public static final String JBOSS_CONFIG_DIR = "jboss.server.config.dir";

	// PR payload
	public static final String PULL_REQUEST = "pull_request";
	public static final String BODY = "body";
	public static final String HEAD = "head";
	public static final String REPO = "repo";
	public static final String FULL_NAME = "full_name";
	public static final String SHA = "sha";
	public static final String TITLE = "title";
	public static final String COMMITS = "commits";

	// Commit payload
	public static final String COMMIT = "commit";
	public static final String MESSAGE = "message";
	public static final String URL = "url";

	public static String readTokenFromProperties(String dirName, String fileName) {
		InputStream is = null;
		File dir = new File(dirName);
		File fileProp = new File(dir, fileName);

		try {
			is = new FileInputStream(fileProp);
			Properties properties = new Properties();
			properties.load(is);

			if (properties.getProperty(TOKEN_PROPERTY) != null) {
				return properties.getProperty(TOKEN_PROPERTY);
			}
		} catch (Exception e) {
			// intentionally ignored
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// intentionally ignored
			}
		}

		return null;
	}
}
