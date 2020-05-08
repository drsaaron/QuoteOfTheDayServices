# Start from alpine image
FROM alpine

# setup timezone
RUN apk add tzdata
ENV TZ=US/Central
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# install java
RUN apk add openjdk11-jdk

# this is the prod environment
ENV ENVIRONMENT prod

# create a workdir
WORKDIR /app

# add the target directory, which has the jars
ADD ./target ./target

# add a shell script to run the java program
ADD ./runServices.sh ./runServices.sh

# run the script
CMD ./runServices.sh

