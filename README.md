# GlobalKinetic Automation Framework

A comprehensive test automation framework setting up Web, API, and Mobile automation testing using Java, Playwright, REST-assured, and Appium.

## Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Test Sections](#test-sections)
- [Test Reports](#test-reports)

## Overview

This framework provides a unified solution for:
- **Section 2**: Web Automation with Playwright (Sauce Demo)
- **Section 3**: API Automation with REST-assured (Spotify API)
- **Section 4**: Mobile Automation with Appium (Android - My Demo App)


## Prerequisites

### Required Software

1. **Java JDK 11 or higher**
   - Download from: https://www.oracle.com/africa/java/technologies/downloads/
   - Verify installation: `java -version`

2. **Gradle 8.5 or higher**
   - Included via Gradle Wrapper (no separate installation needed)
   - Or install manually: https://gradle.org/install/

3. **For Web Automation (Section 2)**
   - Playwright browsers will be installed automatically via Gradle

4. **For API Automation (Section 3)**
   - Postman collection and environment can be found under resourcs on this framework
   - No additional setup will be required, the necessary dependencies are managed by gradle

5. **For Mobile Automation (Section 4)**
   - **Appium Server** (version 2.x)
     - Install: `npm install -g appium`
     - Install UiAutomator2 driver: `appium driver install uiautomator2`
     - Start Appium: `appium`
   
   - **Android SDK**
     - Install Android Studio: https://developer.android.com/studio
     - Set ANDROID_HOME environment variable
     - Add platform-tools to PATH
   
   - **Android Emulator or Physical Device**
     - Create an AVD (Android Virtual Device) via Android Studio
     - Or connect a physical Android device with USB debugging enabled
     - Ensure developers mode is turned on for appium execution to take place
   
   - **My Demo App APK (v1.3.0)**
     - Download from: https://github.com/saucelabs/my-demo-app-rn/releases/tag/v1.3.0
     - Place APK in project root or update `app.path` in config.properties or set up appActivity and appPackage

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd GlobalKineticAuto
```

### 2. Install Dependencies

The framework uses Gradle wrapper. Run:

```bash
./gradlew build
```

This will:
- Download Gradle wrapper (if not present)
- Download all dependencies
- Install Playwright browsers
- Compile the project

### 3. Configure the Framework

Edit `src/test/resources/config.properties` or `src/main/resources/config.properties`:

```properties
# Browser Configuration
browser.type=chromium
browser.headless=false

# Appium Configuration
appium.server.url=http://127.0.0.1:4723
device.name=Android Emulator
platform.version=11.0
app.package=com.saucelabs.mydemoapp.rn
app.activity=.MainActivity
app.path=/path/to/my-demo-app-rn-1.3.0.apk

# Spotify API Configuration 
spotify.access.token=your_access_token_here, you can generate using postman collection after setting up your spotify credentials
```

### 4. Setup Mobile Testing (Section 4)

1. **Start Appium Server:**
   ```bash
   appium
   ```

2. **Start Android Emulator:**
   - Via Android Studio AVD Manager, or
   - Via command line: `emulator -avd <avd_name>`

3. **Verify Device Connection:**
   ```bash
   adb devices
   ```

4. **Install APK (if not using app.path):**
   ```bash
   adb install my-demo-app-rn-1.3.0.apk
   ```

## Configuration

### Test Data Management

Test data is stored in JSON files under `src/test/resources/testdata/`:

- `web-test-data.json` - Web automation test data
- `api-test-data.json` - API automation test data
- `mobile-test-data.json` - Mobile automation test data

Example:
```json
{
  "validUser": {
    "username": "standard_user",
    "password": "secret_sauce"
  }
}
```

### Environment Variables

You can override or edit configuration via environment variables:
- `BROWSER_TYPE` - Browser type (chromium, firefox, webkit)
- `BROWSER_HEADLESS` - Run browser in headless mode
- `APPIUM_SERVER_URL` - Appium server URL
- `SPOTIFY_ACCESS_TOKEN` - Spotify API access token

## Running Tests

### Run All Tests

```bash
./gradlew test
```

### Run Specific Test Suite

**Web Automation Tests:**
```bash
./gradlew test --tests "com.globalkinetic.web.tests.*"
```

**API Automation Tests:**
```bash
./gradlew test --tests "com.globalkinetic.api.tests.*"
```

**Mobile Automation Tests:**
```bash
./gradlew test --tests "com.globalkinetic.mobile.tests.*"
```

### Run Specific Test Class

```bash
./gradlew test --tests "com.globalkinetic.web.tests.SauceDemoSmokeTests"
```

### Run Tests with TestNG XML

```bash
./gradlew test -PsuiteFile=src/test/resources/testng.xml
```

### Run Tests in Parallel

The TestNG suite is configured for parallel execution. Modify `testng.xml` to adjust thread count:

```xml
<suite name="GlobalKinetic Automation Test Suite" parallel="methods" thread-count="3">
```

## Test Sections

### Section 2: Web Automation with Playwright

**System Under Test:** https://www.saucedemo.com/v1/

**Test Cases:**
1. `testLoginPageLoads` - Verify login page loads successfully
2. `testSuccessfulLogin` - Verify successful login with valid credentials
3. `testInvalidLogin` - Verify login fails with invalid credentials
4. `testAddProductToCart` - Verify user can add product to cart
5. `testLogout` - Verify user can logout successfully

**Framework Features:**
- Page Object Model (POM) pattern
- Base page class with common methods
- Screenshot capture on failure

### Section 3: API Automation with REST-assured

**System Under Test:** Spotify Web API (https://developer.spotify.com/documentation/web-api)

**Test Cases:**
1. `testSearchArtists` - Verify search artists endpoint
2. `testGetArtist` - Verify GET artist endpoint
3. `testGetAlbum` - Verify GET album endpoint
4. `testGetTrack` - Verify GET track endpoint
5. `testResponseTime` - Verify response time is acceptable
6. `testResponseHeaders` - Verify response contains required headers
7. `testCreatePlaylist` - Verify POST album endpoint
8. `testFollowArtist` - Verify PUT album endpoint
9. `testUnfollowArtist` - Verify DELETE album endpoint


**Postman Collection:**
- Import `Spotify API Collection.postman_collection.json` into Postman
- Import `Spotfy API Env.postman_environment.json` for environment variables
- Set `spotify_access_token` variable in Postman environment

**Framework Features:**
- Base API client with common HTTP methods
- Request/Response logging
- Response validation
- JSON schema validation support

### Section 4: Mobile Automation with Appium

**System Under Test:** My Demo App RN v1.3.0 (Android)
- Download: https://github.com/saucelabs/my-demo-app-rn/releases/tag/v1.3.0

**Test Cases:**
1. `testLoginPageLoads` - Verify login page loads successfully
2. `testSuccessfulLogin` - Verify successful login with valid credentials
3. `testInvalidLogin` - Verify login fails with invalid credentials
4. `testNavigationToProductDetails` - Verify navigation to product details
5. `testAddProductToCart` - Verify data entry - add product to cart
6. `testLogout` - Verify logout functionality

**Framework Features:**
- Page Object Model (POM) pattern
- Android based platfom
- Test data management
- Implicit and explicit waits

**Platform Differences:**
- Android uses UiAutomator2 for element identification
- Locators use XPath with content-desc for accessibility
- App package and activity configuration for app launch

## Test Reports

### TestNG Reports

After test execution, reports are generated in:
```
build/reports/tests/test/
```

Open `index.html` in a browser to view the test report.

### Custom Reports

Text-based reports are generated in:
```
test-results/reports/test-report.txt
```

### Screenshots

Screenshots are captured on test failures in:
```
test-results/screenshots/
```

## Additional Resources

- [Playwright Documentation](https://playwright.dev/java/)
- [REST-assured Documentation](https://rest-assured.io/)
- [Appium Documentation](http://appium.io/docs/en/about-appium/intro/)
- [TestNG Documentation](https://testng.org/doc/documentation-main.html)
- [Gradle Documentation](https://docs.gradle.org/)

## Contact

For questions or issues, please get hold of Zwikhodo at email id: zwikhodonemadodzi@gmail.com

---

**Note:** This framework follows Java best practices and industry standards for test automation. All tests are designed to be maintainable, scalable, and easy to understand.

