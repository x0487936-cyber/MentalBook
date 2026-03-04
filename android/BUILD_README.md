# MentalBook Android Build Guide

This directory contains the MentalBook Android application with tools to build it completely offline without requiring any external prerequisites (other than Java/JDK which is now installed on your system).

## Prerequisites

- **Java/JDK 17** - Already installed on your system
- **Internet connection** - Required only for the first build to download Android SDK components

## Quick Start

### First Build (Requires Internet)

Run the offline build script once to set up the Android SDK:

```bash
cd android
./build_offline.sh
```

This will:
1. Download and install Android SDK command-line tools
2. Install required SDK components (platform 34, build-tools)
3. Build the debug APK

### Subsequent Builds (Can Work Offline)

After the first build, you can use the simple build script:

```bash
cd android
./build.sh
```

Or directly with Gradle:

```bash
cd android
./gradlew assembleDebug
```

## Build Scripts

| Script | Description |
|--------|-------------|
| `build_offline.sh` | Complete offline build - downloads SDK on first run, then builds |
| `build.sh` | Quick build script for subsequent builds |
| `gradlew` | Gradle wrapper script (uses bundled Gradle 7.5) |

## Output

The built APK will be located at:
```
app/build/outputs/apk/debug/app-debug.apk
```

## Offline Usage

After the initial setup with internet, you can build completely offline:
1. The Android SDK is stored locally in `android-sdk/`
2. Gradle 7.5 is bundled in `gradle-7.5/`
3. All dependencies are cached in `.gradle/`

To build offline after initial setup, edit `build.sh` and add `--offline` flag to the Gradle command, or simply ensure no network-dependent tasks are triggered.

## Troubleshooting

### "Java not found" error
Ensure Java is in your PATH or set JAVA_HOME:
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
```

### Build fails with "SDK not found"
Run `./build_offline.sh` first to set up the Android SDK.

### Clean rebuild
```bash
rm -rf app/build
./build.sh
```

