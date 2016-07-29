#!/bin/bash

# Get the directory
__dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"


cd "$__dir/../../java/src"

# Create the java class files
echo "Create the java class files"
javac -cp ".:./lib/PDFRenderer-0.9.0-improved.jar:./lib/jbig2.jar:./lib/jzebra.jar:./lib/org.json-20120521.jar" -g -verbose de/westwing/printer/ninja/NinjaPrinter.java

# Create the Jar file
echo ""
echo "Create the Jar file"
jar -cvmf META-INF/manifest.mf ../../chrome-extension/host/NinjaPrinter.jar *

# Clean up
echo ""
echo "Cleaning up"
find . -type f -name *.class -exec rm -v {} \;

echo ""
echo "Done!"
