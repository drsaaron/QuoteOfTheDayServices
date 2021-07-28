#! /bin/sh

version=$(getPomAttribute.sh version | sed -e 's/-[A-Z]*$//')
imageName=$(dockerImageName.sh)

docker build -t drsaaron/$imageName .
docker tag drsaaron/$imageName drsaaron/$imageName:$version
