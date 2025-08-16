#!/usr/bin/env bash
set -euo pipefail

# Build debug APK for the Score Counter app
# Prerequisites:
# - ANDROID SDK installed (sdk.dir set in local.properties OR ANDROID_SDK_ROOT/ANDROID_HOME set)
# - Java 17 available

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT_DIR"

if ! command -v ./gradlew >/dev/null 2>&1; then
  echo "Gradle wrapper not found. Generating..."
  if command -v gradle >/dev/null 2>&1; then
    gradle wrapper
  else
    echo "Error: gradle wrapper and gradle not found. Please install Gradle or use Android Studio to generate wrapper." >&2
    exit 1
  fi
fi

# Show environment quick info
echo "JAVA: $(java -version 2>&1 | head -n1 || true)"
[ -f local.properties ] && echo "local.properties present" || echo "local.properties missing"

# Build APK (skip lint/tests for speed)
./gradlew assembleDebug -x lint -x test

APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
if [ -f "$APK_PATH" ]; then
  echo "\nAPK built: $APK_PATH"
  ls -lh "$APK_PATH"
else
  echo "\nBuild finished but APK not found. Check Gradle output above." >&2
  exit 2
fi
