#!/usr/bin/env bash

## A sample script to install the artifact directory contents into a local maven repository

# Script accepts one arg which is the CDI TCK version
if [ $# -eq 0 ]
  then
    echo "No argument was supplied - please provide an argument that is the CDI TCK version!"
    exit 1
fi


if [[ $1 =~ ^[0-9]+\.[0-9]+\.[0-9]+.*$ ]]; then
  VERSION="$1"
else
    echo "Invalid CDI TCK version detected!"
fi

# Parent pom
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=cdi-tck-parent-"$VERSION".pom -DgroupId=org.jboss.cdi.tck \
-DartifactId=cdi-tck-parent -Dversion="$VERSION" -Dpackaging=pom

# Porting Package APIs for CDI TCK
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=cdi-tck-api-"$VERSION".jar -Dsources=cdi-tck-api-"$VERSION"-sources.jar \
-Djavadoc=cdi-tck-api-"$VERSION"-javadoc.jar

# CDI TCK Installed Library - test bean archive
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=cdi-tck-ext-lib-"$VERSION".jar

# CDI TCK Test Suites
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=cdi-tck-core-impl-"$VERSION".jar -Dsources=cdi-tck-core-impl-"$VERSION"-sources.jar
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=cdi-tck-web-impl-"$VERSION".jar -Dsources=cdi-tck-web-impl-"$VERSION"-sources.jar