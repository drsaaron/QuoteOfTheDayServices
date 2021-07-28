#! /bin/sh

imageName=$(dockerImageName.sh)
containerName=$imageName

docker stop $containerName

