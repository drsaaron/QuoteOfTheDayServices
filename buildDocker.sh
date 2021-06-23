#! /bin/sh

version=$(getPomAttribute.sh version | sed -e 's/-[A-Z]*$//')

docker build -t drsaaron/qotdservices .
docker tag drsaaron/qotdservices drsaaron/qotdservices:$version
