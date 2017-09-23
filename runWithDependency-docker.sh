#! /bin/sh

# cd to the app directory
cd ~/quoteOfTheDayServices

# run
export CLASSPATH=$(echo target/*.jar target/dependency/*.jar | sed 's/ /:/g')
java com.blazartech.products.qotdp.rest.Main


