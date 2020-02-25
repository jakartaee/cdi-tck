#!/usr/bin/env bash

## A sample script to install the artifcat directory contents into a local maven repository

# Script accepts one arg which is the CDI TCK version
if [ $# -eq 0 ]
  then
    echo "No argument was supplied - please provide an argument that is the CDI TCK version!"
    exit 1
fi

if [[ $1 =~ ^[0-9]+\.[0-9]+\.[0-9]+.*$ ]]; then
    # Parent pom
    mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
    -Dfile=cdi-tck-parent-"$1".pom -DgroupId=org.jboss.cdi.tck \
    -DartifactId=cdi-tck-parent -Dversion="$1" -Dpackaging=pom

    # Porting Package APIs for CDI TCK
    mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
    -Dfile=cdi-tck-api-"$1".jar -Dsources=cdi-tck-api-"$1"-sources.jar \
    -Djavadoc=cdi-tck-api-"$1"-javadoc.jar

    # CDI TCK Installed Library - test bean archive
    mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
    -Dfile=cdi-tck-ext-lib-"$1".jar

    # CDI TCK Test Suite
    mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
    -Dfile=cdi-tck-impl-"$1".jar -Dsources=cdi-tck-impl-"$1"-sources.jar
else
    echo "Invalid CDI TCK version detected!"
fi