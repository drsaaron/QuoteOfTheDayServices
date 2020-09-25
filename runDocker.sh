#! /bin/ksh

# get the IP address of the host
ip=$(ifconfig wlo1 | grep inet | awk '$1=="inet" {print $2}')
#ip=$(nslookup blazartech-test.csl2otan97lp.us-east-2.rds.amazonaws.com | grep Address| tail -1 | awk '{ print $2 }')

# run the service
docker run --add-host quoteDBServer:$ip -it -p 3000:3000 -v ~/.blazartech:/root/.blazartech drsaaron/qotdservices
