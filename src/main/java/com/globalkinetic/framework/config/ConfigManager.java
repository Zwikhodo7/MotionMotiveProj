package com.globalkinetic.framework.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

/**
 * Configuration Manager for handling test configuration
 */
public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private Configuration config;

    private ConfigManager() {
        try {
            Configurations configs = new Configurations();
            java.net.URL testConfigUrl = Thread.currentThread().getContextClassLoader().getResource("config.properties");
            if (testConfigUrl == null) {
                testConfigUrl = getClass().getClassLoader().getResource("config.properties");
            }
            
            if (testConfigUrl != null) {
                config = configs.properties(testConfigUrl);
                logger.info("Configuration loaded successfully from classpath: {}", testConfigUrl);
            } else {
                File configFile = new File("src/test/resources/config.properties");
                if (!configFile.exists()) {
                    configFile = new File("src/main/resources/config.properties");
                }
                if (!configFile.exists()) {
                    throw new RuntimeException("Configuration file not found in classpath or file system");
                }
                config = configs.properties(configFile);
                logger.info("Configuration loaded successfully from: {}", configFile.getAbsolutePath());
            }
        } catch (Exception e) {
            logger.error("Error loading configuration", e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public String getProperty(String key, String defaultValue) {
        return config.getString(key, defaultValue);
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }
}

