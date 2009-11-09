Generating the signature file
-----------------------------

The general command for generating a signature file looks like this:

java -jar sigtestdev.jar Setup -classpath "%JAVA_HOME%\jre\lib\rt.jar:cdi-api.jar:javax.inject.jar:el-api.jar:jboss-interceptor-api.jar" -Package javax.decorator -Package javax.enterprise -FileName cdi-api.sig -static

Usually all the required libraries will be located in a local Maven repository, so here's a working example with the classpaths filled out:

java -jar  "/home/shane/java/sigtest-2.1/lib/sigtestdev.jar" Setup -classpath "/usr/local/java/jre/lib/rt.jar:/home/shane/.m2/repository/javax/enterprise/cdi-api/1.0.0-SNAPSHOT/cdi-api-1.0.0-SNAPSHOT.jar:/home/shane/.m2/repository/javax/inject/javax.inject/1.0-PFD-1/javax.inject-1.0-PFD-1.jar:/home/shane/.m2/repository/javax/el/el-api/2.1.2-b04/el-api-2.1.2-b04.jar:/home/shane/.m2/repository/org/jboss/interceptor/jboss-interceptor-api/3.1.0-CR1/jboss-interceptor-api-3.1.0-CR1.jar" -Package javax.decorator -Package javax.enterprise -FileName cdi-api.sig -static

Running the signature test
--------------------------
      
To run the signature test simply change the execution command from Setup to SignatureTest:
 
java -jar sigtestdev.jar SignatureTest -classpath "%JAVA_HOME%\jre\lib\rt.jar:cdi-api.jar:javax.inject.jar:el-api.jar:jboss-interceptor-api.jar" -Package javax.decorator -Package javax.enterprise -FileName cdi-api.sig -static

Once again, here's a working example:

java -jar  "/home/shane/java/sigtest-2.1/lib/sigtestdev.jar" SignatureTest -classpath "/usr/local/java/jre/lib/rt.jar:/home/shane/.m2/repository/javax/enterprise/cdi-api/1.0.0-SNAPSHOT/cdi-api-1.0.0-SNAPSHOT.jar:/home/shane/.m2/repository/javax/inject/javax.inject/1.0-PFD-1/javax.inject-1.0-PFD-1.jar:/home/shane/.m2/repository/javax/el/el-api/2.1.2-b04/el-api-2.1.2-b04.jar:/home/shane/.m2/repository/org/jboss/interceptor/jboss-interceptor-api/3.1.0-CR1/jboss-interceptor-api-3.1.0-CR1.jar" -Package javax.decorator -Package javax.enterprise -FileName cdi-api.sig -static

