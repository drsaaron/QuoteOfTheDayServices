#! /bin/sh

version=$(getPomAttribute.sh version | sed -e 's/-SNAPSHOT//')

docker push drsaaron/qotdservices:$version
