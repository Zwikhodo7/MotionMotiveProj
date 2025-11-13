package com.globalkinetic.api;

import com.globalkinetic.framework.config.ConfigManager;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

/**
 * Spotify API Client for testing Spotify Web API
 */
public class SpotifyApiClient extends BaseApiClient {
    private static final Logger logger = LoggerFactory.getLogger(SpotifyApiClient.class);
    private static final String SPOTIFY_BASE_URI = "https://api.spotify.com/v1";
    private String accessToken;

    public SpotifyApiClient() {
        super();
        setBaseUri(SPOTIFY_BASE_URI);
        initializeAccessToken();
    }

    private void initializeAccessToken() {
        ConfigManager config = ConfigManager.getInstance();
        String rawToken = config.getProperty("spotify.access.token", "");
        accessToken = rawToken != null ? rawToken.trim() : "";
        
        logger.debug("Raw token from config: '{}' (length: {})", 
            rawToken != null && rawToken.length() > 10 ? rawToken.substring(0, 10) + "..." : rawToken,
            rawToken != null ? rawToken.length() : 0);
        logger.debug("Trimmed token length: {}", accessToken.length());

        if (!accessToken.isEmpty()) {
            setAuthToken(accessToken);
            logger.info("Spotify access token configured (length: {})", accessToken.length());
        } else {
            logger.error("Spotify access token not configured or empty. Raw value: '{}'", rawToken);
        }
    }

    public Response searchArtists(String query) {
        logger.info("Searching for artists: {}", query);

        String token = accessToken != null ? accessToken.trim() : "";
        if (token.isEmpty()) {
            logger.error("Access token is empty! Token from config: '{}'", accessToken);
            throw new RuntimeException("Spotify access token is not configured");
        }

        Response response = given()
                .baseUri(SPOTIFY_BASE_URI)
                .header("Authorization", "Bearer " + token)
                .queryParam("q", query)
                .queryParam("type", "artist")
                .queryParam("limit", "10")
                .when()
                .get("/search")
                .then()
                .extract()
                .response();
        logger.info("Search response status: {}", response.getStatusCode());
        if (response.getStatusCode() >= 400) {
            logger.warn("Error response body: {}", response.getBody().asString());
        }
        return response;
    }

    public Response getArtist(String artistId) {
        logger.info("Getting artist: {}", artistId);
        String token = accessToken != null ? accessToken.trim() : "";
        if (token.isEmpty()) {
            throw new RuntimeException("Spotify access token is not configured");
        }
        String fullUrl = SPOTIFY_BASE_URI + "/artists/" + artistId;
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(fullUrl)
                .then()
                .extract()
                .response();
    }

    public Response getAlbum(String albumId) {
        logger.info("Getting album: {}", albumId);
        String token = accessToken != null ? accessToken.trim() : "";
        if (token.isEmpty()) {
            throw new RuntimeException("Spotify access token is not configured");
        }
        String fullUrl = SPOTIFY_BASE_URI + "/albums/" + albumId;
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(fullUrl)
                .then()
                .extract()
                .response();
    }

    public Response getTrack(String trackId) {
        logger.info("Getting track: {}", trackId);
        String token = accessToken != null ? accessToken.trim() : "";
        if (token.isEmpty()) {
            throw new RuntimeException("Spotify access token is not configured");
        }
        String fullUrl = SPOTIFY_BASE_URI + "/tracks/" + trackId;
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(fullUrl)
                .then()
                .extract()
                .response();
    }

    public Response getUserProfile() {
        logger.info("Getting current user profile");
        String token = accessToken != null ? accessToken.trim() : "";
        if (token.isEmpty()) {
            throw new RuntimeException("Spotify access token is not configured");
        }
        String fullUrl = SPOTIFY_BASE_URI + "/me";
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(fullUrl)
                .then()
                .extract()
                .response();
    }

    public Response followArtists(String... artistIds) {
        logger.info("Following artists: {}", java.util.Arrays.toString(artistIds));
        String token = accessToken != null ? accessToken.trim() : "";
        if (token.isEmpty()) {
            throw new RuntimeException("Spotify access token is not configured");
        }
        
        String ids = String.join(",", artistIds);
        
        return given()
                .baseUri(SPOTIFY_BASE_URI)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .put("/me/following?type=artist&ids=" + ids)
                .then()
                .extract()
                .response();
    }

    public Response unfollowArtists(String... artistIds) {
        logger.info("Unfollowing artists: {}", java.util.Arrays.toString(artistIds));
        String token = accessToken != null ? accessToken.trim() : "";
        if (token.isEmpty()) {
            throw new RuntimeException("Spotify access token is not configured");
        }
        
        String ids = String.join(",", artistIds);
        
        return given()
                .baseUri(SPOTIFY_BASE_URI)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/me/following?type=artist&ids=" + ids)
                .then()
                .extract()
                .response();
    }

    public Response createPlaylist(String userId, String playlistName, boolean isPublic, String description) {
        logger.info("Creating playlist: {} for user: {}", playlistName, userId);
        String token = accessToken != null ? accessToken.trim() : "";
        if (token.isEmpty()) {
            throw new RuntimeException("Spotify access token is not configured");
        }

        java.util.Map<String, Object> requestBody = new java.util.HashMap<>();
        requestBody.put("name", playlistName);
        requestBody.put("public", isPublic);
        if (description != null && !description.isEmpty()) {
            requestBody.put("description", description);
        }
        
        return given()
                .baseUri(SPOTIFY_BASE_URI)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/users/" + userId + "/playlists")
                .then()
                .extract()
                .response();
    }
}

