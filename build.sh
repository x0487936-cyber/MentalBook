#!/bin/bash
# Build script for VirtualXander
# Configures classpath and compiles all Java sources

set -e

echo "============================================"
echo "  VirtualXander Build Script"
echo "============================================"
echo ""

# Define paths
SRC_DIR="src"
BIN_DIR="bin"
MAIN_CLASS="VirtualXander"
JAR_FILE="VirtualXander.jar"

# Clean and create bin directory
echo "Step 1: Setting up build directory..."
rm -rf "$BIN_DIR"
mkdir -p "$BIN_DIR"

# Compile all Java files
echo "Step 2: Compiling Java sources..."
echo "  Source directory: $SRC_DIR"
echo "  Output directory: $BIN_DIR"
echo ""

# Compile main class from root, GUI app, and other classes from src
javac -d "$BIN_DIR" -sourcepath "$SRC_DIR" "$SRC_DIR"/*.java VirtualXander.java MentalBookApp.java

echo "✓ Compilation successful!"
echo ""

# Create JAR file
echo "Step 3: Creating JAR file..."
cat > manifest.tmp << EOF
Manifest-Version: 1.0
Main-Class: $MAIN_CLASS
Class-Path: $BIN_DIR/
EOF

jar cfm "$JAR_FILE" manifest.tmp -C "$BIN_DIR" .
# Add root-level classes to JAR (only existing files)
for class_file in VirtualXander.class; do
    if [ -f "$class_file" ]; then
        jar uf "$JAR_FILE" "$class_file"
    fi
done

rm -f manifest.tmp

echo "✓ JAR file created: $JAR_FILE"
echo ""

# Display file sizes
echo "Build Summary:"
echo "  - JAR file: $JAR_FILE ($(du -h "$JAR_FILE" | cut -f1))"
echo "  - Classes: $(ls "$BIN_DIR"/*.class 2>/dev/null | wc -l) files"
echo ""

# Verify the JAR
echo "Verifying JAR contents:"
jar tf "$JAR_FILE" | head -20
echo "  ... (truncated)"
echo ""

echo "============================================"
echo "  Build Complete!"
echo "============================================"
echo ""
echo "To run VirtualXander:"
echo "  java -jar $JAR_FILE"
echo ""
echo "Or using classpath:"
echo "  java -cp $BIN_DIR $MAIN_CLASS"
echo ""

