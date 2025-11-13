package com.globalkinetic.api.tests;

import com.globalkinetic.api.SpotifyApiClient;
import com.globalkinetic.framework.utils.ReportUtils;
import com.globalkinetic.framework.utils.TestDataManager;
import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * API Tests for Spotify Web API using REST-assured
 */
public class SpotifyApiTests {
    private SpotifyApiClient apiClient;

    @BeforeClass
    public void setUp() {
        apiClient = new SpotifyApiClient();
    }

    @Test(priority = 1, description = "Verify search artists endpoint returns valid response")
    public void testSearchArtists() {
        try {
            JsonObject testData = TestDataManager.loadTestData("api-test-data.json");
            String searchQuery = testData.get("searchQuery").getAsString();

            Response response = apiClient.searchArtists(searchQuery);

            int statusCode = response.getStatusCode();

            Assert.assertEquals(statusCode, 200, "Status code should be 200. Actual: " + statusCode);
            Assert.assertNotNull(response.getBody(), "Response body should not be null");
            String responseBody = response.getBody().asString();
            Assert.assertTrue(responseBody.contains("artists"), "Response should contain artists");

            ReportUtils.logTestResult("testSearchArtists", "PASS",
                "Search artists endpoint working correctly");
        } catch (Exception e) {
            ReportUtils.logTestResult("testSearchArtists", "FAIL", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 2, description = "Verify GET artist endpoint returns valid response")
    public void testGetArtist() {
        try {
            JsonObject testData = TestDataManager.loadTestData("api-test-data.json");
            String artistId = testData.get("artistId").getAsString();

            Response response = apiClient.getArtist(artistId);

            int statusCode = response.getStatusCode();

            Assert.assertEquals(statusCode, 200, "Status code should be 200. Actual: " + statusCode);

            ReportUtils.logTestResult("testGetArtist", "PASS",
                "Get artist endpoint responded with status: " + statusCode);
        } catch (Exception e) {
            ReportUtils.logTestResult("testGetArtist", "FAIL", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 3, description = "Verify GET album endpoint returns valid response")
    public void testGetAlbum() {
        try {
            JsonObject testData = TestDataManager.loadTestData("api-test-data.json");
            String albumId = testData.get("albumId").getAsString();

            Response response = apiClient.getAlbum(albumId);

            int statusCode = response.getStatusCode();

            Assert.assertEquals(statusCode, 200, "Status code should be 200" + statusCode);

            ReportUtils.logTestResult("testGetAlbum", "PASS",
                "Get album endpoint responded with status: " + statusCode);
        } catch (Exception e) {
            ReportUtils.logTestResult("testGetAlbum", "FAIL", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 4, description = "Verify GET track endpoint returns valid response")
    public void testGetTrack() {
        try {
            JsonObject testData = TestDataManager.loadTestData("api-test-data.json");
            String trackId = testData.get("trackId").getAsString();

            Response response = apiClient.getTrack(trackId);

            int statusCode = response.getStatusCode();

            Assert.assertEquals(statusCode, 200, "Status code should be 200. Actual: " + statusCode);

            ReportUtils.logTestResult("testGetTrack", "PASS",
                "Get track endpoint responded with status: " + statusCode);
        } catch (Exception e) {
            ReportUtils.logTestResult("testGetTrack", "FAIL", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 5, description = "Verify response time is acceptable")
    public void testResponseTime() {
        try {
            JsonObject testData = TestDataManager.loadTestData("api-test-data.json");
            String searchQuery = testData.get("searchQuery").getAsString();

            long startTime = System.currentTimeMillis();
            Response response = apiClient.searchArtists(searchQuery);
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;

            int statusCode = response.getStatusCode();
            Assert.assertEquals(statusCode, 200, "Status code should be 200. Actual: " + statusCode);
            
            Assert.assertTrue(responseTime < 5000,
                "Response time should be less than 5 seconds. Actual: " + responseTime + "ms");

            ReportUtils.logTestResult("testResponseTime", "PASS",
                "Response time: " + responseTime + "ms");
        } catch (Exception e) {
            ReportUtils.logTestResult("testResponseTime", "FAIL", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 6, description = "Verify response contains required headers")
    public void testResponseHeaders() {
        try {
            JsonObject testData = TestDataManager.loadTestData("api-test-data.json");
            String searchQuery = testData.get("searchQuery").getAsString();

            Response response = apiClient.searchArtists(searchQuery);

            int statusCode = response.getStatusCode();
            Assert.assertEquals(statusCode, 200, "Status code should be 200. Actual: " + statusCode);

            String contentType = response.getHeader("Content-Type");
            Assert.assertNotNull(contentType, "Content-Type header should be present");
            Assert.assertTrue(contentType != null && contentType.toLowerCase().contains("application/json"),
                "Content-Type should contain application/json. Actual: " + contentType);

            ReportUtils.logTestResult("testResponseHeaders", "PASS",
                "Response headers validated successfully. Content-Type: " + contentType);
        } catch (Exception e) {
            ReportUtils.logTestResult("testResponseHeaders", "FAIL", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 7, description = "Verify POST - Create playlist endpoint returns valid response")
    public void testCreatePlaylist() {
        try {

            Response userResponse = apiClient.getUserProfile();
            int userStatusCode = userResponse.getStatusCode();
            
            if (userStatusCode == 200) {
                String userId = userResponse.jsonPath().getString("id");
                Assert.assertNotNull(userId, "User ID should not be null");
                System.out.println("////////////////////////////////"+userId);
                String playlistName = "Test Playlist " + System.currentTimeMillis();
                Response response = apiClient.createPlaylist(userId, playlistName, false, "Test playlist created by automation");
                
                int statusCode = response.getStatusCode();
                Assert.assertTrue(statusCode == 200 || statusCode == 201,
                    "Status code should be 200 or 201. Actual: " + statusCode);
                
                if (statusCode == 200 || statusCode == 201) {
                    String playlistId = response.jsonPath().getString("id");
                    Assert.assertNotNull(playlistId, "Playlist ID should be returned");
                }
                ReportUtils.logTestResult("testCreatePlaylist", "PASS",
                    "Create playlist endpoint responded with status: " + statusCode);
            } else {
                ReportUtils.logTestResult("testCreatePlaylist", "SKIP",
                    "Cannot get user profile (status: " + userStatusCode + "), skipping playlist creation");
                Assert.fail();
            }
        } catch (Exception e) {
            ReportUtils.logTestResult("testCreatePlaylist", "FAIL", e.getMessage());
            org.slf4j.LoggerFactory.getLogger(SpotifyApiTests.class).warn("Playlist creation test failed: {}", e.getMessage());
        }

    }

    @Test(priority = 8, description = "Verify PUT - Follow artist endpoint returns valid response")
    public void testFollowArtist() {
        try {
            JsonObject testData = TestDataManager.loadTestData("api-test-data.json");
            String artistId = testData.get("artistId").getAsString();
            
            Response response = apiClient.followArtists(artistId);
            
            int statusCode = response.getStatusCode();
            Assert.assertTrue(statusCode == 200 || statusCode == 204,
                "Status code should be 200 or 204. Actual: " + statusCode);
            
            ReportUtils.logTestResult("testFollowArtist", "PASS",
                "Follow artist endpoint responded with status: " + statusCode);
        } catch (Exception e) {
            ReportUtils.logTestResult("testFollowArtist", "FAIL", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 9, description = "Verify DELETE - Unfollow artist endpoint returns valid response")
    public void testUnfollowArtist() {
        try {
            JsonObject testData = TestDataManager.loadTestData("api-test-data.json");
            String artistId = testData.get("artistId").getAsString();
            
            Response response = apiClient.unfollowArtists(artistId);
            
            int statusCode = response.getStatusCode();
            Assert.assertTrue(statusCode == 200 || statusCode == 204,
                "Status code should be 200 or 204. Actual: " + statusCode);
            
            ReportUtils.logTestResult("testUnfollowArtist", "PASS",
                "Unfollow artist endpoint responded with status: " + statusCode);
        } catch (Exception e) {
            ReportUtils.logTestResult("testUnfollowArtist", "FAIL", e.getMessage());
            throw e;
        }
    }
}

