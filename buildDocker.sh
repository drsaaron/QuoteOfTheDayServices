#! /bin/sh

version=$(getPomAttribute.sh version | sed -e 's/-[A-Z]*$//')
imageName=$(getPomAttribute.sh artifactId | tr '[:upper:]' '[:lower:]')

docker build -t drsaaron/$imageName .
docker tag drsaaron/$imageName drsaaron/$imageName:$version
