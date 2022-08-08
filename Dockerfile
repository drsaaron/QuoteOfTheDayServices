FROM drsaaron/blazarjavabase:1.9

ENV ENVIRONMENT=prod

# add the pom
ADD ./pom.xml ./pom.xml

# add the target directory, which has the jars
ADD ./target ./target

# add a shell script to run the java program
ADD ./runServices.sh ./runServices.sh

# add a healthcheck
ENV SERVER_PORT=3000
HEALTHCHECK CMD curl --silent --fail http://localhost:$SERVER_PORT/monitoring/health || exit 1

# run the script
CMD ./runServices.sh

