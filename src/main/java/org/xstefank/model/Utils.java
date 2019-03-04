package org.xstefank.model;

import java.net.MalformedURLException;
import java.net.URL;

public class Utils {

    public static final String GITHUB_BASE = "https://api.github.com";
    public static final String CONFIG_FILE = "config.properties";
    public static final String TOKEN_PROPERTY = "github.oauth.token";
    public static final String TOKEN_ENV = "GITHUB_OAUTH_TOKEN";
    public static final String JBOSS_CONFIG_DIR = "jboss.server.config.dir";
    public static final String TEMPLATE_FORMAT_URL = "template.format.url";
    public static final String USERLIST_FILE_NAME = "user-list.txt";
    public static final String ADMINLIST_FILE_NAME = "admin-list.txt";
    public static final String WHITELIST_ENABLED = "whitelist.enabled";

    //PR payload
    public static final String PULL_REQUEST = "pull_request";
    public static final String BODY = "body";
    public static final String HEAD = "head";
    public static final String SHA = "sha";
    public static final String TITLE = "title";
    public static final String COMMITS = "commits";
    public static final String NUMBER = "number";
    public static final String REF = "ref";
    public static final String BASE = "base";
    public static final String COMMITS_URL = "commits_url";

    //Issue payload
    public static final String ISSUE = "issue";
    public static final String COMMENT = "comment";
    public static final String USER = "user";
    public static final String LOGIN = "login";
    public static final String ACTION = "action";

    //Commit payload
    public static final String COMMIT = "commit";
    public static final String MESSAGE = "message";
    public static final String URL = "url";

    public static URL getFormatUrl() throws MalformedURLException {
        String target = System.getProperty(TEMPLATE_FORMAT_URL);
        if (target == null)
            target = TyrProperties.getProperty(TEMPLATE_FORMAT_URL);
        return target != null ? new URL(target) : null;
    }
}