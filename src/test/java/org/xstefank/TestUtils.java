package org.xstefank;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.xstefank.model.yaml.FormatConfig;

import java.io.File;
import java.io.IOException;

public class TestUtils {

    public static final String YAML_DIR = "yaml";
    public static final String JSON_DIR = "json";

    public static final JsonNode TEST_PAYLOAD = loadJson(JSON_DIR + "/testPayload.json");
    public static final JsonNode BAD_TEST_PAYLOAD = loadJson(JSON_DIR + "/badTestPayload.json");
    public static final JsonNode EMPTY_PAYLOAD = createEmptyJsonPayload();
    public static final FormatConfig FOMAT_CONFIG = loadFormatFromYamlFile(YAML_DIR + "/testTemplate.yaml");

    public static FormatConfig loadFormatFromYamlFile(String fileName) {
        try {
            File file = new File(TestUtils.class.getClassLoader().getResource(fileName).getFile());
            return new ObjectMapper(new YAMLFactory()).readValue(file, FormatConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load file " + fileName);
        }
    }

    private static JsonNode loadJson(String fileName) {
        try {
            File file = new File(TestUtils.class.getClassLoader().getResource(fileName).getFile());
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

}