#!/bin/bash
# Offline Android Build Script
# This script builds the Android app without requiring any external dependencies
# It downloads and sets up Android SDK components locally within the project

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ANDROID_SDK_DIR="$SCRIPT_DIR/android-sdk"
CMDLINE_TOOLS_DIR="$ANDROID_SDK_DIR/cmdline-tools"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "============================================"
echo "  MentalBook Android Offline Build Script"
echo "============================================"
echo ""

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check for Java
echo "Step 1: Checking prerequisites..."
if ! command_exists java; then
    echo -e "${RED}Error: Java is not installed. Please install JDK first.${NC}"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
echo -e "${GREEN}✓${NC} Java found (version $JAVA_VERSION)"

# Check for javac
if ! command_exists javac; then
    echo -e "${RED}Error: JDK (javac) is not installed. Please install JDK.${NC}"
    exit 1
fi
echo -e "${GREEN}✓${NC} JDK (javac) found"

# Export JAVA_HOME if not set
if [ -z "$JAVA_HOME" ]; then
    JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
    export JAVA_HOME
    echo "  Set JAVA_HOME to: $JAVA_HOME"
fi
echo ""

# Step 2: Setup Android SDK
echo "Step 2: Setting up Android SDK..."

# Create SDK directory
mkdir -p "$ANDROID_SDK_DIR"
mkdir -p "$CMDLINE_TOOLS_DIR"

# Check if cmdline-tools already exists
if [ -d "$CMDLINE_TOOLS_DIR/latest" ]; then
    echo -e "${GREEN}✓${NC} Android SDK command-line tools already present"
else
    echo "Downloading Android SDK command-line tools..."
    
    # Determine OS
    OS=$(uname -s | tr '[:upper:]' '[:lower:]')
    ARCH=$(uname -m)
    
    if [ "$ARCH" = "x86_64" ]; then
        ARCH="x64"
    elif [ "$ARCH" = "aarch64" ]; then
        ARCH="arm64"
    fi
    
    CMDLINE_TOOLS_URL="https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip"
    CMDLINE_TOOLS_ZIP="/tmp/cmdline-tools.zip"
    
    echo "  URL: $CMDLINE_TOOLS_URL"
    echo "  Downloading..."
    
    if command_exists curl; then
        curl -L -o "$CMDLINE_TOOLS_ZIP" "$CMDLINE_TOOLS_URL"
    elif command_exists wget; then
        wget -O "$CMDLINE_TOOLS_ZIP" "$CMDLINE_TOOLS_URL"
    else
        echo -e "${RED}Error: Neither curl nor wget is available.${NC}"
        exit 1
    fi
    
    echo "  Extracting..."
    unzip -q -o "$CMDLINE_TOOLS_ZIP" -d "$CMDLINE_TOOLS_DIR"
    
    # Rename to 'latest' if needed
    if [ -d "$CMDLINE_TOOLS_DIR/cmdline-tools" ]; then
        mv "$CMDLINE_TOOLS_DIR/cmdline-tools" "$CMDLINE_TOOLS_DIR/latest"
    fi
    
    rm -f "$CMDLINE_TOOLS_ZIP"
    echo -e "${GREEN}✓${NC} Android SDK command-line tools installed"
fi

# Step 3: Accept licenses and install required SDK components
echo ""
echo "Step 3: Installing Android SDK components..."

SDK_MANAGER="$CMDLINE_TOOLS_DIR/latest/bin/sdkmanager"

# Create local.properties
echo "sdk.dir=$ANDROID_SDK_DIR" > "$SCRIPT_DIR/local.properties"
echo "  Created local.properties"

# Accept licenses
mkdir -p "$ANDROID_SDK_DIR/licenses"
echo "24333f8a63b6825ea9c5514f83c2829b004d1fee" > "$ANDROID_SDK_DIR/licenses/android-sdk-license"
echo "84831b9409646a918e30573bab4c9c91346d8abd" >> "$ANDROID_SDK_DIR/licenses/android-sdk-license"
echo "d56f5187479451eabf01fb78af6dfcb131a6481e" >> "$ANDROID_SDK_DIR/licenses/android-sdk-license"
echo -e "${GREEN}✓${NC} SDK licenses accepted"

# Install required SDK components
echo "  Installing SDK platform 34..."
yes | "$SDK_MANAGER" "platforms;android-34" --sdk_root="$ANDROID_SDK_DIR" 2>/dev/null || true

echo "  Installing build-tools..."
yes | "$SDK_MANAGER" "build-tools;34.0.0" --sdk_root="$ANDROID_SDK_DIR" 2>/dev/null || true

echo "  Installing platform-tools..."
yes | "$SDK_MANAGER" "platform-tools" --sdk_root="$ANDROID_SDK_DIR" 2>/dev/null || true

echo -e "${GREEN}✓${NC} SDK components installed"
echo ""

# Step 4: Build the Android app
echo "Step 4: Building Android app..."

cd "$SCRIPT_DIR"

# Use local Gradle
GRADLE_HOME="$SCRIPT_DIR/gradle-7.5"
export PATH="$GRADLE_HOME/bin:$PATH"

# Build with offline mode
echo "  Running Gradle build..."
"$GRADLE_HOME/bin/gradle" assembleDebug --offline -p "$SCRIPT_DIR" 2>&1 || {
    echo -e "${YELLOW}Warning: Offline build failed, trying online build...${NC}"
    "$GRADLE_HOME/bin/gradle" assembleDebug -p "$SCRIPT_DIR" 2>&1
}

# Check if build was successful
if [ -f "$SCRIPT_DIR/app/build/outputs/apk/debug/app-debug.apk" ]; then
    APK_SIZE=$(du -h "$SCRIPT_DIR/app/build/outputs/apk/debug/app-debug.apk" | cut -f1)
    echo ""
    echo "============================================"
    echo -e "${GREEN}  Build Successful!${NC}"
    echo "============================================"
    echo ""
    echo "  APK Location: $SCRIPT_DIR/app/build/outputs/apk/debug/app-debug.apk"
    echo "  APK Size: $APK_SIZE"
    echo ""
    echo "  To install on device:"
    echo "    adb install app/build/outputs/apk/debug/app-debug.apk"
    echo ""
else
    echo -e "${RED}Error: Build failed. Check logs above.${NC}"
    exit 1
fi

