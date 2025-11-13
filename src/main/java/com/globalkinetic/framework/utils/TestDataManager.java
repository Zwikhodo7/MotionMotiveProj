package com.globalkinetic.framework.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Test Data Manager for handling test data from JSON files
 */
public class TestDataManager {
    private static final Logger logger = LoggerFactory.getLogger(TestDataManager.class);
    private static final Map<String, JsonObject> dataCache = new HashMap<>();

    public static JsonObject loadTestData(String fileName) {
        if (dataCache.containsKey(fileName)) {
            return dataCache.get(fileName);
        }

        try {
            File file = new File("src/test/resources/testdata/" + fileName);
            if (!file.exists()) {
                file = new File("src/main/resources/testdata/" + fileName);
            }
            
            String jsonContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            JsonObject jsonObject = JsonParser.parseString(jsonContent).getAsJsonObject();
            dataCache.put(fileName, jsonObject);
            logger.info("Test data loaded from: {}", fileName);
            return jsonObject;
        } catch (IOException e) {
            logger.error("Error loading test data from: {}", fileName, e);
            throw new RuntimeException("Failed to load test data: " + fileName, e);
        }
    }
}
