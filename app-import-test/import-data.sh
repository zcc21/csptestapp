#!/bin/sh
export MAVEN_OPTS="-Xmx2048m -XX:MaxPermSize=256m"
set -e
mvn clean install
cd target
sh import-test-data.sh