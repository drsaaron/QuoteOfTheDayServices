# Start from the java docker.
FROM openjdk:latest

# create a workdir
WORKDIR /app

# add the target directory, which has the jars
ADD ./target ./target

# add a shell script to run the java program
ADD ./runWithDependency-docker.sh ./runWithDependency.sh

# run the script
CMD ./runWithDependency.sh

