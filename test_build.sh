#!/bin/sh

## This is the script to build test rest api. ##
export MAVEN_OPTS="-Xmx512m -XX:MaxPermSize=256m"
mvn clean install -DskipTests