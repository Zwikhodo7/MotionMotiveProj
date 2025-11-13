package com.globalkinetic.api;

import com.globalkinetic.framework.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base API Client for REST API testing using REST-assured
 */
public class BaseApiClient {
    protected static final Logger logger = LoggerFactory.getLogger(BaseApiClient.class);
    protected RequestSpecification requestSpec;
    protected ConfigManager configManager;

    public BaseApiClient() {
        configManager = ConfigManager.getInstance();
        initializeRequestSpec();
    }

    private void initializeRequestSpec() {
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    public void setBaseUri(String baseUri) {
        RestAssured.baseURI = baseUri;
        logger.info("Base URI set to: {}", baseUri);
    }

    public void setAuthToken(String token) {
        requestSpec = new RequestSpecBuilder()
                .addRequestSpecification(requestSpec)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        logger.info("Authorization token set");
    }
}

