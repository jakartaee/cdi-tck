Notes on using Sigtest
-----------------------------------------
Sigtest home page: https://sigtest.dev.java.net/
User Guide: http://java.sun.com/javame/sigtest/docs/sigtest2.1_usersguide.pdf

Generating a signature file
---------------------------
To generate a signature file, use the following command:

java -cp %JAVA_HOME%\jre\lib\rt.jar%;sigtestdev.jar com.sun.tdk.signaturetest.Setup -Package javax.annotation -Package javax.context -Package javax.decorator -Package javax.event -Package javax.inject -Package javax.interceptor -Package javax.weld -FileName weld.sig -Classpath .;%JAVA_HOME%\jre\lib\rt.jar -static -Exclude java
