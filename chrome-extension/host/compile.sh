#!/bin/bash

# Get the directory of the current file
__dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Cd into the java host root
cd "$__dir/../../java"

# Create the java class files
echo "Run ant"
ant

# Display results
echo ""
echo "Done!"

