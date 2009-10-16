CDI TCK
------------

Contexts and Dependency Injection (CDI) for Java EE (JSR-299) is a new Java standard for
dependency injection and contextual lifecycle management. This is the TCK for
CDI

This distribution, as a whole, is licensed under the terms of the Apache Public
License (see apl.txt).

Documentation can be found at
http://seamframework.org/Weld/WeldDevelopmentOverview

This distribution consists of:

artifacts/
   -- TCK binaries, sources, javadoc, packaged as jars
   -- specification audit (source and report)
   -- TestNG suite.xml file for running the TCK

doc/
   -- Reference guide for the TCK

lib/
   -- Libraries dependencies for running the TCK

weld/
   -- TCK runner for using Weld (the CDI RI) with JBoss AS

   jboss-as/
      -- configuration for running the TCK with JBoss AS
   
   jboss-tck-runner/
      -- maven based TCK runner for JBoss AS

   lib/
      -- all libraries for the Weld implementation of the CDI porting package
   

Generating the TCK audit coverage report
----------------------------------------
Run the following command to generate the TCK audit report:

  mvn clean install -Dtck-audit
