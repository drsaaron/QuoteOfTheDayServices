#! /bin/ksh

docker kill $(docker ps | grep qotdservices | awk '{ print $1 }')
