#!/bin/bash
# Build script for creating .exe package using Launch4j
# This creates a native Windows executable from the JAR

set -e

echo "============================================"
echo "  Building .exe Package for VirtualXander"
echo "============================================"
echo ""

# Check if JAR exists
JAR_FILE="MentalBook.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "Error: $JAR_FILE not found!"
    echo "Run ./build.sh first to create the JAR file."
    exit 1
fi

# Output directory
OUTPUT_DIR="dist"
mkdir -p "$OUTPUT_DIR"

# Configuration
APP_NAME="VirtualXander"
APP_VERSION="0.2.0.0"
EXE_FILE="$OUTPUT_DIR/VirtualXander.exe"

# Download Launch4j if not present
LAUNCH4J_DIR="launch4j"
LAUNCH4J_ZIP="launch4j.zip"
LAUNCH4J_URL="https://sourceforge.net/projects/launch4j/files/launch4j-3.50/launch4j-3.50-linux.tgz/download"
LAUNCH4J_BIN="$LAUNCH4J_DIR/launch4j"

echo "Step 1: Checking for Launch4j..."
if [ ! -f "$LAUNCH4J_BIN" ]; then
    echo "  Downloading Launch4j..."
    mkdir -p "$LAUNCH4J_DIR"
    
    # Try different download methods
    if command -v wget &> /dev/null; then
        wget -q --show-progress -O "$LAUNCH4J_DIR/launch4j.tgz" "$LAUNCH4J_URL" || \
        wget -q -O "$LAUNCH4J_DIR/launch4j.tgz" "$LAUNCH4J_URL"
    elif command -v curl &> /dev/null; then
        curl -sL "$LAUNCH4J_URL" -o "$LAUNCH4J_DIR/launch4j.tgz"
    fi
    
    if [ -f "$LAUNCH4J_DIR/launch4j.tgz" ]; then
        tar -xzf "$LAUNCH4J_DIR/launch4j.tgz" -C "$LAUNCH4J_DIR" --strip-components=1
        rm -f "$LAUNCH4J_DIR/launch4j.tgz"
    fi
fi

# Check if Launch4j is available
if [ ! -f "$LAUNCH4J_BIN" ]; then
    echo "Error: Could not download Launch4j"
    echo "Please install Launch4j manually from: https://launch4j.sourceforge.net/"
    exit 1
fi

chmod +x "$LAUNCH4J_BIN"

echo "Step 2: Creating Launch4j configuration..."
cat > launch4j.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<launch4jConfig>
  <dontUsePrivateJre>false</dontUsePrivateJre>
  <headerType>gui</headerType>
  <jar>MentalBook.jar</jar>
  <outfile>$EXE_FILE</outfile>
  <downloadUrl>https://java.com/download</downloadUrl>
  <initialMemorySize>256</initialMemorySize>
  <maxMemorySize>1024</maxMemorySize>
  <minVersion>1.8.0</minVersion>
  <runtimeBits>64</runtimeBits>
  <synchedByOtherThread>false</synchedByOtherThread>
  <customProcName>false</customProcName>
  <enableSandbox>false</enableSandbox>
  <cmdLine></cmdLine>
  <icon></icon>
  <jfx>false</jfx>
  <useLauncherTypes>true</useLauncherTypes>
  <singleInstance>
    <mutexName>MentalBook</mutexName>
    <dialogText>MentalBook is already running</dialogText>
  </singleInstance>
  <classPath>
    <mainClass>MentalBookApp</mainClass>
    <cp>MentalBook.jar</cp>
  </classPath>
  <jre>
    <path></path>
    <bundledJrePath></bundledJrePath>
    <minVersion></minVersion>
    <maxVersion></maxVersion>
    <jdkPreference>preferJre</jdkPreference>
    <runtimeBits>64</runtimeBits>
  </jre>
  <messages>
    <startupErr>An error occurred while starting MentalBook</startupErr>
    <bundledJreErr>A bundled JRE could not be found</bundledJreErr>
    <jreVersionErr>Java version mismatch</jreVersionErr>
    <launcherErr>Could not find the launcher</launcherErr>
  </messages>
</launch4jConfig>
EOF

echo "Step 3: Building Windows executable..."
"$LAUNCH4J_BIN" launch4j.xml

echo ""
echo "Step 4: Verifying output..."
if [ -f "$EXE_FILE" ]; then
    echo "  Success! Created: $EXE_FILE"
    ls -lh "$EXE_FILE"
else
    echo "Error: .exe was not created"
    exit 1
fi

echo ""
echo "============================================"
echo "  Build Complete!"
echo "============================================"
echo ""
echo "Output file: $EXE_FILE"
echo ""
echo "Note: The .exe requires Java Runtime Environment (JRE) to be installed on Windows."
echo "You can also bundle the JRE with the executable by modifying the Launch4j config."
echo ""

