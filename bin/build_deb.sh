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
APP_NAME="virtualxander"
APP_VERSION="0.2.0.2"
DEB_FILE="$OUTPUT_DIR/${APP_NAME}_${APP_VERSION}_amd64.deb"

# Create temporary build directory
BUILD_DIR="/tmp/VirtualXander-deb-build"
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR/DEBIAN"
mkdir -p "$BUILD_DIR/usr/share/$APP_NAME"
mkdir -p "$BUILD_DIR/usr/bin"

# Create icon directories
mkdir -p "$BUILD_DIR/usr/share/icons/hicolor/16x16/apps"
mkdir -p "$BUILD_DIR/usr/share/icons/hicolor/32x32/apps"
mkdir -p "$BUILD_DIR/usr/share/icons/hicolor/48x48/apps"
mkdir -p "$BUILD_DIR/usr/share/icons/hicolor/64x64/apps"
mkdir -p "$BUILD_DIR/usr/share/icons/hicolor/128x128/apps"
mkdir -p "$BUILD_DIR/usr/share/icons/hicolor/256x256/apps"
mkdir -p "$BUILD_DIR/usr/share/icons/hicolor/512x512/apps"
mkdir -p "$BUILD_DIR/usr/share/applications"

echo "Step 1: Copying files..."
# Copy JAR file
cp "$JAR_FILE" "$BUILD_DIR/usr/share/$APP_NAME/"

# Copy icon files
cp icons/mentalbook_16.png "$BUILD_DIR/usr/share/icons/hicolor/16x16/apps/mentalbook.png"
cp icons/mentalbook_32.png "$BUILD_DIR/usr/share/icons/hicolor/32x32/apps/mentalbook.png"
cp icons/mentalbook_48.png "$BUILD_DIR/usr/share/icons/hicolor/48x48/apps/mentalbook.png"
cp icons/mentalbook_64.png "$BUILD_DIR/usr/share/icons/hicolor/64x64/apps/mentalbook.png"
cp icons/mentalbook_128.png "$BUILD_DIR/usr/share/icons/hicolor/128x128/apps/mentalbook.png"
cp icons/mentalbook_256.png "$BUILD_DIR/usr/share/icons/hicolor/256x256/apps/mentalbook.png"
cp icons/mentalbook_512.png "$BUILD_DIR/usr/share/icons/hicolor/512x512/apps/mentalbook.png"

# Copy desktop file
cp icons/mentalbook.desktop "$BUILD_DIR/usr/share/applications/"

# Create wrapper script
cat > "$BUILD_DIR/usr/bin/$APP_NAME" << 'EOF'
#!/bin/bash
# VirtualXander launcher script
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
Maintainer: Xander Thompson <x0487936@gmail.com>
Description: VirtualXander - Your Companion App
 A friendly virtual companion application powered by Xander's personal feelings and reactions.
 It provides mental health support, conversation, and various helpful features.
EOF

# Create postinst script (runs after installation)
cat > "$BUILD_DIR/DEBIAN/postinst" << 'EOF'
#!/bin/bash
# Post-installation script
chmod 755 /usr/bin/mentalbook
chmod 644 /usr/share/mentalbook/MentalBook.jar
chmod 644 /usr/share/icons/hicolor/*/apps/mentalbook.png
chmod 644 /usr/share/applications/mentalbook.desktop
# Update desktop database for icon to appear in launcher
if command -v update-desktop-database >/dev/null 2>&1; then
    update-desktop-database /usr/share/applications 2>/dev/null || true
fi
# Update icon cache
if command -v gtk-update-icon-cache >/dev/null 2>&1; then
    gtk-update-icon-cache -f -t /usr/share/icons/hicolor 2>/dev/null || true
fi
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
