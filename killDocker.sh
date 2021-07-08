#! /bin/ksh

imageName=$(getPomAttribute.sh artifactId | tr '[:upper:]' '[:lower:]')
containerName=$imageName

docker stop $containerName

