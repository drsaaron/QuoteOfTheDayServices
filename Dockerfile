FROM drsaaron/blazarjavabase:1.0

# add the target directory, which has the jars
ADD ./target ./target

# add a shell script to run the java program
ADD ./runServices.sh ./runServices.sh

# run the script
CMD ./runServices.sh

