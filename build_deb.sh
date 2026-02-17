#!/bin/bash
# Alternative build script for creating .deb package manually
# Creates a proper Debian package structure

set -e

echo "============================================"
echo "  Building .deb Package for VirtualXander"
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
rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR"

# Package details
APP_NAME="mentalbook"
APP_VERSION="0.2.0.0"
DEB_FILE="$OUTPUT_DIR/${APP_NAME}_${APP_VERSION}_amd64.deb"

# Create temporary build directory
BUILD_DIR="/tmp/VirtualXander-deb-build"
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR/DEBIAN"
mkdir -p "$BUILD_DIR/usr/share/$APP_NAME"
mkdir -p "$BUILD_DIR/usr/bin"

echo "Step 1: Copying files..."
# Copy JAR file
cp "$JAR_FILE" "$BUILD_DIR/usr/share/$APP_NAME/"

# Create wrapper script
cat > "$BUILD_DIR/usr/bin/$APP_NAME" << 'EOF'
#!/bin/bash
# MentalBook launcher script
APP_DIR="/usr/share/mentalbook"
java -jar "$APP_DIR/MentalBook.jar" "$@"
EOF
chmod +x "$BUILD_DIR/usr/bin/$APP_NAME"

echo "Step 2: Creating package control files..."

# Create control file
cat > "$BUILD_DIR/DEBIAN/control" << EOF
Package: $APP_NAME
Version: $APP_VERSION
Section: utils
Priority: optional
Architecture: amd64
Depends: openjdk-17-jre | java17-runtime
Maintainer: Xander Thompson <xander@example.com>
Description: MentalBook - Your Companion App
 A friendly virtual companion application powered by Xander's personal feelings and reactions.
 It provides mental health support, conversation, and various helpful features.
EOF

# Create postinst script (runs after installation)
cat > "$BUILD_DIR/DEBIAN/postinst" << 'EOF'
#!/bin/bash
# Post-installation script
chmod 755 /usr/bin/mentalbook
chmod 644 /usr/share/mentalbook/MentalBook.jar
exit 0
EOF
chmod +x "$BUILD_DIR/DEBIAN/postinst"

# Create prerm script (runs before removal)
cat > "$BUILD_DIR/DEBIAN/prerm" << 'EOF'
#!/bin/bash
# Pre-removal script
exit 0
EOF
chmod +x "$BUILD_DIR/DEBIAN/prerm"

echo "Step 3: Building .deb package..."
dpkg-deb --build "$BUILD_DIR" "$DEB_FILE"

echo ""
echo "Step 4: Verifying output..."
if [ -f "$DEB_FILE" ]; then
    echo "  Success! Created: $DEB_FILE"
    ls -lh "$DEB_FILE"
else
    echo "Error: .deb was not created"
    exit 1
fi

# Cleanup
rm -rf "$BUILD_DIR"

echo ""
echo "============================================"
echo "  Build Complete!"
echo "============================================"
echo ""
echo "Output file: $DEB_FILE"
echo ""
echo "You can install it with: sudo dpkg -i $DEB_FILE"
echo "Or: sudo apt install ./$DEB_FILE"
echo ""
