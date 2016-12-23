#!/bin/sh

cd test-${project.version}

set -e

csp_version=${parent.parent.version}

isMac=false
uname|grep Darwin && isMac=true

if $isMac; then
	sed -i "" 's/<version>csp.version<\/version>/<version>'`echo ${csp_version}`'<\/version>/' pom-test.xml
else
	sed -i 's/<version>csp.version<\/version>/<version>'`echo ${csp_version}`'<\/version>/' pom-test.xml
fi
mvn process-test-classes -f pom-test.xml
mvn test -f pom-test.xml -DappRootDir=../../../webapp/target/${csp.cloud.app.name}-webapp-${project.version}