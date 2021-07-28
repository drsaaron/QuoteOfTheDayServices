#! /bin/sh

# get the IP address of the host
ip=$(ifconfig wlo1 | grep inet | awk '$1=="inet" {print $2}')
#ip=$(nslookup blazartech-test.csl2otan97lp.us-east-2.rds.amazonaws.com | grep Address| tail -1 | awk '{ print $2 }')

# run the service
imageName=$(dockerImageName.sh)
containerName=$imageName

docker stop $containerName
docker rm $containerName
docker run --name $containerName --add-host quoteDBServer:$ip -d -p 3000:3000 -v ~/.blazartech:/root/.blazartech drsaaron/$imageName
