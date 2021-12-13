Generating the signature file
-----------------------------

You can use maven to generate a signature file, but most of the time, you should be fine using already provided signature files under ${TCK_REPO}/impl/src/main/resources.

Generate Using the Maven Profile
-----------------------

There is an added profile in ${TCK_REPO}/impl called signature-generation.
Firstly make sure that you have the desired Java version set on your path.
You can then execute the build by running a command such as:

mvn clean install -Psignature-generation -Djdk.major.version=${JDK}


This will generate a file named "cdi-api-jdk${JDK}.sig" (e.g. cdi-api-jdk11.sig) and located in ${TCK_REPO}/impl/target.
Note that the JDK variable only affects file naming and nothing else.

Check Using the Maven sigtest-pom.xml
-----------------------


JDK 11 signature tests
---------------------------

You need a different signature file for each major JDK version.
Currently, there is only JDK 11 version included.

cdi-api-jdk11.sig is a signature file built with JDK 11 and can be found under ${TCK_REPO}/impl/src/main/resources/cdi-api-jdk11.sig
