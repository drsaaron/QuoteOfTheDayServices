#! /bin/sh

version=$(getPomAttribute.sh version | sed -e 's/-[A-Z]*$//')
imageName=$(dockerImageName.sh)

docker push drsaaron/$imageName:$version
