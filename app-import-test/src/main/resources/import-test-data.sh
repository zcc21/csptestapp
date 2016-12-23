#!/bin/sh

if [ -d test-${project.version} ]; then
	cd test-${project.version}
else
	mkdir test-${project.version}
	cd test-${project.version}
	jar -xvf ../${csp.cloud.app.cmr.applicationid}-test-${project.version}.jar
fi

csp_version=${parent.parent.version}

isMac=false
uname|grep Darwin && isMac=true

if $isMac; then
	sed -i "" 's/<version>csp.version<\/version>/<version>'`echo ${csp_version}`'<\/version>/' pom-load.xml
else
	sed -i 's/<version>csp.version<\/version>/<version>'`echo ${csp_version}`'<\/version>/' pom-load.xml
fi

#specify newConfigJsonFile if needed, eg.: ../myconfig.json
#authUserInConfigOnly: if true only users in config.json will have the authority to app, otherwise users not in config.json will be unauthorized to app
mvn clean install -f pom-load.xml \
	-DappRootDir=../../../webapp/target/${csp.cloud.app.name}-webapp-${project.version} \
	-DdbUsername=${hibernate.connection.username} \
	-DdbPassword=${hibernate.connection.password} \
	-DdbUrl=${hibernate.connection.url} \
	-DnewConfigJsonFile= \
	-DauthUserInConfigOnly=false \
	-DhibernateCfgDir=../../../app-dal-codegen/target/classes