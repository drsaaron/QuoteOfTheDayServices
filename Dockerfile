# Start from the java docker.
FROM openjdk:latest

## CREATE APP USER ## 
ENV APP_USER=app

# Create the home directory for the new app user. 
RUN mkdir -p /home/$APP_USER

# Create an app user so our program doesn't run as root. 
RUN groupadd -r $APP_USER &&\ 
useradd -r -g $APP_USER -d /home/app -s /sbin/nologin -c "Docker image user" $APP_USER 

# Set the home directory to our app user's home. 
ENV HOME=/home/$APP_USER
ENV APP_HOME=$HOME/quoteOfTheDayServices

## SETTING UP THE APP ## 
RUN mkdir $APP_HOME 
WORKDIR $APP_HOME 

# add the target directory, which has the jars
ADD ./target $APP_HOME/target

# add the crypto file data, which for some reason can't be read from ~/.blazartech
#ADD ./bt /root/.blazartech

# add a shell script to run the java program
ADD ./runWithDependency-docker.sh $APP_HOME/runWithDependency.sh

# run the script
CMD $APP_HOME/runWithDependency.sh

# Chown all the files to the app user. 
RUN chown -R $APP_USER:$APP_USER $APP_HOME 

# Change to the app user. 
USER $APP_USER