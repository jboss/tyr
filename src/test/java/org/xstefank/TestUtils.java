package org.xstefank;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.xstefank.model.yaml.Format;
import org.xstefank.model.yaml.FormatConfig;
import org.xstefank.model.yaml.SkipPatterns;
import java.io.File;
import java.io.IOException;

public class TestUtils {

    public static final String YAML_DIR = "yaml";
    public static final String JSON_DIR = "json";
    public static final String TARGET_DIR = "target";

    public static final JsonNode TEST_PAYLOAD = loadJson(JSON_DIR + "/testPayload.json");
    public static final JsonNode BAD_TEST_PAYLOAD = loadJson(JSON_DIR + "/badTestPayload.json");
    public static final JsonNode ISSUE_PAYLOAD = loadJson(JSON_DIR + "/issuePayload.json");
    public static final JsonNode EMPTY_PAYLOAD = createEmptyJsonPayload();
    public static final JsonNode TEST_COMMITS_PAYLOAD = loadJson(JSON_DIR + "/testCommitsPayload.json");
    public static final JsonNode MULTIPLE_COMMIT_MESSAGES_PAYLOAD = loadJson(JSON_DIR + "/multipleCommitMessagesPayload.json");

    public static final String READ_TOKEN = "readToken";
    public static final String GET_JSON_WITH_COMMITS = "getJsonWithCommits";
    public static final FormatConfig FORMAT_CONFIG = loadFormatFromYamlFile(YAML_DIR + "/testTemplate.yaml");
    public static final Path TEST_CONFIG_PATH = getFilePath("testConfig.properties");

    public static FormatConfig loadFormatFromYamlFile(String fileName) {
        try {
            File file = getFile(fileName);
            return new ObjectMapper(new YAMLFactory()).readValue(file, FormatConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load file " + fileName);
        }
    }

    public static void deleteFileIfExists(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    public static boolean fileContainsLine(File file, String line) {
        try (FileReader fileReader = new FileReader(file);
             BufferedReader br = new BufferedReader(fileReader)) {
            String fileLine;
            while ((fileLine = br.readLine()) != null) {
                if (fileLine.equals(line)) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read file located in " + file.getPath(), e);
        }
        return false;
    }

    public static void writeUsernameToFile(String username, File file) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(username);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot write username to file", e);
        }
    }

    private static JsonNode loadJson(String fileName) {
        try {
            File file = getFile(fileName);
            return new ObjectMapper().readTree(file);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load file "+ fileName);
        }
    }

    private static JsonNode createEmptyJsonPayload() {
        try {
            return new ObjectMapper().readTree("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Path getFilePath(String fileName) {
        try {
            return Paths.get(TestUtils.class.getClassLoader().getResource(fileName).toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Cannot get path of file: " + fileName, e);
        }
    }

    public static FormatConfig setUpFormatConfig(SkipPatterns testSkipPatterns) {
        Format testFormat = new Format();
        testFormat.setSkipPatterns(testSkipPatterns);
        FormatConfig testFormatConfig = new FormatConfig();
        testFormatConfig.setFormat(testFormat);
        return testFormatConfig;
    }

    private static File getFile(String fileName) throws UnsupportedEncodingException {
        String path = TestUtils.class.getClassLoader().getResource(fileName).getFile();
        return new File(URLDecoder.decode(path, "UTF-8"));
    }
}
