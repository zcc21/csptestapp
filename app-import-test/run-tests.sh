#!/bin/sh

export MAVEN_OPTS="-Xmx2048m -XX:MaxPermSize=256m"
set -e
cd target
sh run-test-cases.sh