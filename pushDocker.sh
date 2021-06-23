#! /bin/sh

version=$(getPomAttribute.sh version | sed -e 's/-[A-Z]*$//')

docker push drsaaron/qotdservices:$version
