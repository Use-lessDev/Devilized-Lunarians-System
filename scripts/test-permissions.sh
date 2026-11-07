#!/usr/bin/env sh
set -eu

ROOT="$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)"
OUT="$ROOT/build/test-classes"
rm -rf "$OUT"
mkdir -p "$OUT"

find "$ROOT/src/main/java" "$ROOT/src/test/java" -name '*.java' -print > "$ROOT/build/test-sources.txt"
javac -encoding UTF-8 -source 8 -target 8 -d "$OUT" @"$ROOT/build/test-sources.txt"
java -ea -cp "$OUT" devilized.lunarians.permissions.FeaturePermissionStoreTest
