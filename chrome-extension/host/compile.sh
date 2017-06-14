#!/bin/bash

# Get the directory of the current file
__dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Cd into the java host root
cd "$__dir/../../java"

# Run Java unit tests
echo "Running: ant test"

exec 5>&1
test_output=$(ant test | tee /dev/fd/5)

if [[ $test_output != *"BUILD SUCCESSFUL"* ]]; then
  echo "Java unit tests failed."
  exit
fi

# Create the java class files
echo ""
echo "Running: ant"
ant

# Display results
echo ""
echo "Done!"

