#! /bin/sh

appEnv=${ENVIRONMENT:-test}

java -jar target/QuoteOfTheDayServices-1.*-RELEASE.jar --spring.config.name=application,$appEnv
