#! /bin/sh

version=$(getPomAttibute.sh version | sed -e 's/-SNAPSHOT//')

docker build -t drsaaron/qotdservices .
docker tag drsaaron/qotdservices drsaaron/qotdservices:$version
