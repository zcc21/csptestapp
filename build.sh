export MAVEN_OPTS="-Xmx2048m -XX:MaxPermSize=256m"
set -e
mvn clean install -DskipTests