#! /bin/ksh

rm -rf target/dependency
mvn dependency:copy-dependencies

