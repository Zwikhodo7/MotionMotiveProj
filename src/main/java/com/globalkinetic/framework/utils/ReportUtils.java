package com.globalkinetic.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for generating test reports
 */
public class ReportUtils {
    private static final Logger logger = LoggerFactory.getLogger(ReportUtils.class);
    private static final String REPORT_DIR = "test-results/reports/";

    public static void logTestResult(String testName, String status, String message) {
        logger.info("Test: {} | Status: {} | Message: {}", testName, status, message);
        
        try {
            File reportDir = new File(REPORT_DIR);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            File reportFile = new File(REPORT_DIR + "test-report.txt");
            try (FileWriter writer = new FileWriter(reportFile, true)) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                writer.write(String.format("[%s] %s - %s: %s%n", timestamp, testName, status, message));
            }
        } catch (IOException e) {
            logger.error("Error writing to report file", e);
        }
    }
}

