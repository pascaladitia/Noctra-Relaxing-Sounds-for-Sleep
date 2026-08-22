#!/usr/bin/env bash
set -euo pipefail

usage() {
    echo "Usage: $0 --name <project-name> --package <package-name> --output <output-dir>"
    echo ""
    echo "Generate a new project from this template."
    echo ""
    echo "  --name     New project name (e.g., MyApp)"
    echo "  --package  New package name (e.g., com.example.myapp)"
    echo "  --output   Output directory for the new project"
    echo ""
    echo "Example:"
    echo "  $0 --name MyApp --package com.example.myapp --output ../MyApp"
    exit 1
}

while [[ $# -gt 0 ]]; do
    case "$1" in
        --name)    NAME="$2"; shift 2 ;;
        --package) PACKAGE="$2"; shift 2 ;;
        --output)  OUTPUT="$2"; shift 2 ;;
        *)         usage ;;
    esac
done

if [[ -z "${NAME:-}" || -z "${PACKAGE:-}" || -z "${OUTPUT:-}" ]]; then
    usage
fi

TEMPLATE_DIR="$(cd "$(dirname "$0")" && pwd)"
OLD_NAME="Noctra"
OLD_PACKAGE="com.pascal.noctra"
OLD_PACKAGE_PATH="com/pascal/Noctra"

NEW_PACKAGE_PATH="${PACKAGE//./\/}"

echo "Generating project..."
echo "  Name:    $OLD_NAME -> $NAME"
echo "  Package: $OLD_PACKAGE -> $PACKAGE"
echo "  Output:  $OUTPUT"

# Copy template
rsync -a --exclude='build/' --exclude='.gradle/' --exclude='.idea/' \
    --exclude='*.iml' --exclude='local.properties' --exclude='xcuserdata/' \
    --exclude='Pods/' --exclude='DerivedData/' \
    --exclude='sharedUI/build/' --exclude='androidApp/build/' \
    --exclude='.git/' \
    "$TEMPLATE_DIR/" "$OUTPUT/"

# Rename Kotlin source directories
rename_package_dir() {
    local base="$1"
    if [[ -d "$base/$OLD_PACKAGE_PATH" ]]; then
        mkdir -p "$base/$NEW_PACKAGE_PATH"
        cp -r "$base/$OLD_PACKAGE_PATH/"* "$base/$NEW_PACKAGE_PATH/"
        rm -rf "$base/$OLD_PACKAGE_PATH"
    fi
}

rename_package_dir "$OUTPUT/sharedUI/src/commonMain/kotlin"
rename_package_dir "$OUTPUT/sharedUI/src/androidMain/kotlin"
rename_package_dir "$OUTPUT/sharedUI/src/iosMain/kotlin"
rename_package_dir "$OUTPUT/androidApp/src/main/kotlin"

# Clean empty old package dirs
find "$OUTPUT" -type d -name 'pascal' -exec rm -rf {} + 2>/dev/null || true

# Replace in all text files (excluding binary and build dirs)
find "$OUTPUT" \
    -type f \( -name '*.kt' -o -name '*.kts' -o -name '*.xml' -o -name '*.properties' \
    -o -name '*.toml' -o -name '*.pro' -o -name '*.swift' -o -name '*.md' \
    -o -name '*.podspec' -o -name 'Podfile' -o -name '*.plist' -o -name '*.xcconfig' \
    -o -name '*.entitlements' -o -name '*.pbxproj' -o -name '*.xcscheme' \) \
    ! -path '*/build/*' ! -path '*/.gradle/*' ! -path '*/Pods/*' ! -path '*/xcuserdata/*' \
    | while read -r file; do

    if file "$file" | grep -q 'text'; then
        sed -i '' "s/$OLD_PACKAGE/$PACKAGE/g" "$file" 2>/dev/null || true
    fi
done

# Replace project name (case-sensitive and case-insensitive patterns)
find "$OUTPUT" \
    -type f ! -path '*/build/*' ! -path '*/.gradle/*' ! -path '*/Pods/*' \
    | while read -r file; do

    if file "$file" | grep -q 'text'; then
        sed -i '' "s/$OLD_NAME/$NAME/g" "$file" 2>/dev/null || true
        old_lower=$(echo "$OLD_NAME" | tr '[:upper:]' '[:lower:]')
        new_lower=$(echo "$NAME" | tr '[:upper:]' '[:lower:]')
        sed -i '' "s/$old_lower/$new_lower/g" "$file" 2>/dev/null || true
    fi
done

# Clean Gradle cache
rm -rf "$OUTPUT/.gradle" "$OUTPUT/sharedUI/build" "$OUTPUT/androidApp/build" 2>/dev/null || true

# Rename Xcode project directory
XCODE_DIR="$OUTPUT/iosApp/iosApp.xcodeproj"
if [[ -d "$XCODE_DIR" ]]; then
    mv "$XCODE_DIR" "$OUTPUT/iosApp/${NAME}.xcodeproj" 2>/dev/null || true
fi

XCODE_WORKSPACE="$OUTPUT/iosApp/iosApp.xcworkspace"
if [[ -d "$XCODE_WORKSPACE" ]]; then
    mv "$XCODE_WORKSPACE" "$OUTPUT/iosApp/${NAME}.xcworkspace" 2>/dev/null || true
fi

echo ""
echo "Project generated at: $OUTPUT"
echo ""
echo "Next steps:"
echo "  1. cd $OUTPUT"
echo "  2. Open androidApp/build.gradle.kts and verify applicationId"
echo "  3. Open sharedUI/build.gradle.kts and verify namespace"
echo "  4. Open androidApp/src/main/AndroidManifest.xml and verify label"
echo "  5. rm -f generate.sh"
echo "  6. git init && git add . && git commit -m 'initial project from template'"
echo ""
