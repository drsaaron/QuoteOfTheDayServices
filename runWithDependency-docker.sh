#! /bin/sh

appEnv=${ENVIRONMENT:-prod}

java -jar target/QuoteOfTheDayServices-1.*-SNAPSHOT.jar --spring.config.name=application,$appEnv
