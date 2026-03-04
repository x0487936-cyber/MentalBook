#!/bin/bash
# Simple Build Script - for building after initial setup
# This script uses the local Gradle and Android SDK

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ANDROID_SDK_DIR="$SCRIPT_DIR/android-sdk"

# Check if SDK is set up
if [ ! -d "$ANDROID_SDK_DIR" ]; then
    echo "Android SDK not found. Running initial setup..."
    ./build_offline.sh
    exit $?
fi

# Set up environment
export JAVA_HOME=${JAVA_HOME:-$(dirname $(dirname $(readlink -f $(which java))))}
export ANDROID_HOME="$ANDROID_SDK_DIR"
export PATH="$ANDROID_SDK_DIR/cmdline-tools/latest/bin:$ANDROID_SDK_DIR/platform-tools:$PATH"

# Ensure local.properties exists
if [ ! -f "$SCRIPT_DIR/local.properties" ]; then
    echo "sdk.dir=$ANDROID_SDK_DIR" > "$SCRIPT_DIR/local.properties"
fi

# Use local Gradle
GRADLE_HOME="$SCRIPT_DIR/gradle-7.5"

echo "Building Android app..."
"$GRADLE_HOME/bin/gradle" assembleDebug -p "$SCRIPT_DIR"

if [ -f "$SCRIPT_DIR/app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo ""
    echo "Build successful!"
    echo "APK: $SCRIPT_DIR/app/build/outputs/apk/debug/app-debug.apk"
else
    echo "Build failed!"
    exit 1
fi

