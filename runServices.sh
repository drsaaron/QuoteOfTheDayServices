#! /bin/sh

appEnv=${ENVIRONMENT:-test}

artifact=$(getPomAttribute.sh artifactId)
version=$(getPomAttribute.sh version)
version=1.31-RELEASE
java -jar target/$artifact-$version.jar --spring.config.name=application,$appEnv
