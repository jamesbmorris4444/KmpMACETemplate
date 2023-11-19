#!/bin/sh
set -eu

# allow overriding the version
VERSION=${SENTRY_CLI_VERSION:-latest}

PLATFORM=`uname -s`
ARCH=`uname -m`

case "$PLATFORM" in
  CYGWIN*) PLATFORM="Windows"
  ;;
  MINGW*) PLATFORM="Windows"
  ;;
  MSYS*) PLATFORM="Windows"
  ;;
  Darwin) ARCH="universal"
  ;;
esac

case "$ARCH" in
  armv6*) ARCH="armv7"
  ;;
  armv7*) ARCH="armv7"
  ;;
  armv8*) ARCH="aarch64"
  ;;
  armv64*) ARCH="aarch64"
  ;;
  aarch64*) ARCH="aarch64"
  ;;
esac

# If the install directory is not set, set it to a default
if [ -z ${INSTALL_DIR+x} ]; then
  INSTALL_DIR=/usr/local/bin
fi
if [ -z ${INSTALL_PATH+x} ]; then
  INSTALL_PATH="${INSTALL_DIR}/sentry-cli"
fi

DOWNLOAD_URL="https://release-registry.services.sentry.io/apps/sentry-cli/${VERSION}?response=download&arch=${ARCH}&platform=${PLATFORM}&package=sentry-cli"

echo "This script will automatically install sentry-cli (${VERSION}) for you."
echo "Installation path: ${INSTALL_PATH}"
if [ "x$(id -u)" = "x0" ]; then
  echo "Warning: this script is currently running as root. This is dangerous. "
  echo "         Instead run it as normal user. We will sudo as needed."
fi

if [ -f "$INSTALL_PATH" ]; then
  echo "error: sentry-cli is already installed."
  echo "  run \"sentry-cli update\" to update to latest version"
  exit 1
fi

if ! hash curl 2> /dev/null; then
  echo "error: you do not have 'curl' installed which is required for this script."
  exit 1
fi

TEMP_FILE=`mktemp "${TMPDIR:-/tmp}/.sentrycli.XXXXXXXX"`
TEMP_HEADER_FILE=`mktemp "${TMPDIR:-/tmp}/.sentrycli-headers.XXXXXXXX"`

cleanup() {
  rm -f "$TEMP_FILE"
  rm -f "$TEMP_HEADER_FILE"
}

trap cleanup EXIT
HTTP_CODE=$(curl -SL --progress-bar "$DOWNLOAD_URL" -D "$TEMP_HEADER_FILE" --output "$TEMP_FILE" --write-out "%{http_code}")
if [ ${HTTP_CODE} -lt 200 ] || [ ${HTTP_CODE} -gt 299 ]; then
  echo "error: your platform and architecture (${PLATFORM}-${ARCH}) is unsupported."
  exit 1
fi

for PYTHON in python3 python2 python ''; do
    if hash "$PYTHON"; then
        break
    fi
done

if [ "$PYTHON" ]; then
  "$PYTHON" - <<EOF "${TEMP_FILE}" "${TEMP_HEADER_FILE}"
if 1:
    import sys
    import re
    import hashlib
    import binascii

    validated = False
    with open(sys.argv[2], "r") as f:
        for line in f:
            match = re.search("(?i)^digest:.?sha256=([^,\n ]+)", line)
            if match is not None:
                with open(sys.argv[1], "rb") as downloaded:
                    hasher = hashlib.sha256()
                    while True:
                        chunk = downloaded.read(4096)
                        if not chunk:
                            break
                        hasher.update(chunk)
                    calculated = hasher.digest()
                    expected = binascii.a2b_base64(match.group(1))
                    if calculated != expected:
                        print("error: checksum mismatch (got %s, expected %s)" % (
                            binascii.b2a_hex(calculated).decode("ascii"),
                            binascii.b2a_hex(expected).decode("ascii")
                        ))
                        sys.exit(1)
                    validated = True
                    break
    if not validated:
        print("warning: unable to validate checksum because no checksum available")
EOF
else
  echo "warning: python not available, unable to verify checksums"
fi

chmod 0755 "$TEMP_FILE"
if ! mv "$TEMP_FILE" "$INSTALL_PATH" 2> /dev/null; then
  sudo -k mv "$TEMP_FILE" "$INSTALL_PATH"
fi

echo "Installed $("$INSTALL_PATH" --version)"

echo 'Done!'
