#!/bin/bash

# Mobile Environment Setup Script
# This script helps set up Android SDK for mobile testing

echo "=== Mobile Automation Setup ==="
echo ""

# Check if Android SDK already exists
if [ -d "$HOME/Library/Android/sdk" ]; then
    ANDROID_HOME="$HOME/Library/Android/sdk"
    echo "✅ Found Android SDK at: $ANDROID_HOME"
elif [ -d "$HOME/Android/Sdk" ]; then
    ANDROID_HOME="$HOME/Android/Sdk"
    echo "✅ Found Android SDK at: $ANDROID_HOME"
else
    echo "❌ Android SDK not found"
    echo ""
    echo "Please install Android Studio from: https://developer.android.com/studio"
    echo "Or follow the command-line setup in MOBILE_SETUP_GUIDE.md"
    exit 1
fi

# Set environment variables
echo ""
echo "Setting up environment variables..."

# Add to .zshrc if not already present
if ! grep -q "ANDROID_HOME" ~/.zshrc 2>/dev/null; then
    echo "" >> ~/.zshrc
    echo "# Android SDK" >> ~/.zshrc
    echo "export ANDROID_HOME=$ANDROID_HOME" >> ~/.zshrc
    echo "export PATH=\$ANDROID_HOME/platform-tools:\$PATH" >> ~/.zshrc
    echo "export PATH=\$ANDROID_HOME/emulator:\$PATH" >> ~/.zshrc
    echo "export PATH=\$ANDROID_HOME/tools:\$PATH" >> ~/.zshrc
    echo "✅ Added Android SDK paths to ~/.zshrc"
else
    echo "⚠️  Android SDK paths already in ~/.zshrc"
fi

# Add Appium to PATH if not present
if ! grep -q "npm-global" ~/.zshrc 2>/dev/null; then
    echo "" >> ~/.zshrc
    echo "# Appium" >> ~/.zshrc
    echo "export PATH=~/.npm-global/bin:\$PATH" >> ~/.zshrc
    echo "✅ Added Appium to PATH"
fi

# Export for current session
export ANDROID_HOME=$ANDROID_HOME
export PATH=$ANDROID_HOME/platform-tools:$PATH
export PATH=$ANDROID_HOME/emulator:$PATH
export PATH=~/.npm-global/bin:$PATH

echo ""
echo "=== Verification ==="
echo "ANDROID_HOME: $ANDROID_HOME"

if command -v adb &> /dev/null; then
    echo "✅ ADB found: $(which adb)"
    adb version | head -1
else
    echo "❌ ADB not found - make sure platform-tools is installed"
fi

if command -v appium &> /dev/null; then
    echo "✅ Appium found: $(which appium)"
    appium --version
else
    echo "❌ Appium not found"
fi

echo ""
echo "=== Next Steps ==="
echo "1. Reload your shell: source ~/.zshrc"
echo "2. Start an Android emulator or connect a device"
echo "3. Verify device: adb devices"
echo "4. Start Appium: appium"
echo "5. Run tests: ./gradlew test --tests \"com.globalkinetic.mobile.tests.*\""
echo ""
echo "For detailed instructions, see: MOBILE_SETUP_GUIDE.md"

