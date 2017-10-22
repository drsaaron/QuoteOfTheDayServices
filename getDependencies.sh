#! /bin/ksh

rm -rf target/dependency
~/netbeans/netbeans-8.2/java/maven/bin/mvn dependency:copy-dependencies

