CD "%1"
%2
SET MAVEN_OPTS=-Xmx2048m -XX:MaxPermSize=512m
mvn clean install -DskipTests