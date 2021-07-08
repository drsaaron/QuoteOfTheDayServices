#! /bin/sh

version=$(getPomAttribute.sh version | sed -e 's/-[A-Z]*$//')
imageName=$(getPomAttribute.sh artifactId | tr '[:upper:]' '[:lower:]')

docker push drsaaron/$imageName:$version
