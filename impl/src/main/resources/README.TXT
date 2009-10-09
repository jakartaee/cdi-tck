Notes on using Sigtest
======================

Sigtest home page: https://sigtest.dev.java.net/
User Guide: http://java.sun.com/javame/sigtest/docs/sigtest2.1_usersguide.pdf

Generating a signature file
===========================
The general command for generating a signature file looks like this:

java -cp %JAVA_HOME%\jre\lib\rt.jar%;sigtestdev.jar com.sun.tdk.signaturetest.Setup -Package javax.decorator -Package javax.enterprise -Package javax.event -Package javax.inject -Package javax.interceptor -FileName weld.sig -Classpath .;%JAVA_HOME%\jre\lib\rt.jar -static 

Here's a working example with the classpaths filled out:

java -cp %JAVA_HOME%\jre\lib\rt.jar;c:/java/sigtest-2.0/lib/sigtestdev.jar com.sun.tdk.signaturetest.Setup -Package javax.decorator -Package javax.enterprise -Package javax.event -Package javax.inject -Package javax.interceptor -FileName weld.sig -Classpath .;%JAVA_HOME%\jre\lib\rt.jar;"C:\Documents and Settings\shane\.m2\repository\org\jboss\weld\jsr299-api\1.0.0-SNAPSHOT\jsr299-api-1.0.0-SNAPSHOT.jar";"C:\Documents and Settings\shane\.m2\repository\javax\el\el-api\1.2\el-api-1.2.jar" -static 

Running the signature test
==========================
To run the signature test simply change the execution class from Setup to SignatureTest:

java -cp %JAVA_HOME%\jre\lib\rt.jar%;sigtestdev.jar com.sun.tdk.signaturetest.SignatureTest -Package javax.decorator -Package javax.enterprise -Package javax.event -Package javax.inject -Package javax.interceptor -FileName weld.sig -Classpath .;%JAVA_HOME%\jre\lib\rt.jar -static 

Here's a working example:

java -cp %JAVA_HOME%\jre\lib\rt.jar;c:/java/sigtest-2.0/lib/sigtestdev.jar com.sun.tdk.signaturetest.SignatureTest -Package javax.decorator -Package javax.enterprise -Package javax.event -Package javax.inject -Package javax.interceptor -FileName weld.sig -Classpath .;%JAVA_HOME%\jre\lib\rt.jar;"C:\Documents and Settings\shane\.m2\repository\org\jboss\weld\jsr299-api\1.0.0-SNAPSHOT\jsr299-api-1.0.0-SNAPSHOT.jar";"C:\Documents and Settings\shane\.m2\repository\javax\el\el-api\1.2\el-api-1.2.jar" -static 


When running the signature test, you may get the following message:

  "The return type java.lang.reflect.Member can't be resolved"
  
This can safely be ignored, the important thing is to get the "STATUS:Passed." message.

Forcing a signature test failure
================================
Just for fun (and to confirm that the signature test is working correctly), you can try the following:

1) Edit weld.sig
2) Modify one of the class signatures - in the following example we change one of the constructors for NullableDependencyException - here's the original:

CLSS public javax.inject.NullableDependencyException
cons public NullableDependencyException()
cons public NullableDependencyException(java.lang.String)

Let's change the constructor parameter to a java.lang.Integer instead:

CLSS public javax.inject.NullableDependencyException
cons public NullableDependencyException()
cons public NullableDependencyException(java.lang.Integer)

3) Now we run the signature test using the above command, which should result in the following errors:

Missing Constructors
--------------------

javax.inject.NullableDependencyException:                   constructor public javax.inject.NullableDependencyException.NullableDependencyException(java.lang.Integer)

Added Constructors
------------------

javax.inject.NullableDependencyException:                   constructor public javax.inject.NullableDependencyException.NullableDependencyException(java.lang.String)


STATUS:Failed.2 errors
