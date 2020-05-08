#! /bin/sh

appEnv=${ENVIRONMENT:-test}

java -jar target/QuoteOfTheDayServices-1.*-SNAPSHOT.jar --spring.config.name=application,$appEnv
