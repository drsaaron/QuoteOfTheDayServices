#! /bin/ksh

appEnv=${ENVIRONMENT:-test}

java -jar target/QuoteOfTheDayServices-1.0-SNAPSHOT.jar --spring.config.name=application,$appEnv
