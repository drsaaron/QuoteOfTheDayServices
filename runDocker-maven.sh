#! /bin/ksh

docker run -p 3000:3000 -v ~/.m2:/root/.m2 -v $PWD:/app -v ~/.blazartech:/root/.blazartech maven mvn exec:java -f /app/pom.xml
