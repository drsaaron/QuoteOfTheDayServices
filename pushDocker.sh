#! /bin/ksh

version=$(grep '^ *<version>' pom.xml | head -1 | perl -pe 's%</?version>%%g; s/-SNAPSHOT//; s/^ *//; s/\W*$//g')

docker push drsaaron/qotdservices:$version
