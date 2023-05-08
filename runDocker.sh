#! /bin/sh

# run the service
imageName=$(dockerImageName.sh)
imageVersion=$(getPomAttribute.sh version | sed 's/-RELEASE$//')
containerName=$(echo $imageName | sed -re 's%^.*/([a-zA-Z]*)$%\1%') # could also use basename $(dockerImageName.sh)

docker stop $containerName
docker rm $containerName
docker run --user $(id -u):$(id -g) --network qotd --name $containerName -d -p 3000:3000 -v ~/.blazartech:/home/$(whoami)/.blazartech $imageName:$imageVersion
