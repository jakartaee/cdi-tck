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

src/
   -- TCK sources (tests porting package API)

lib/
   -- Libraries for running the TCK including binaries of the TCK
   
coverage.html
   -- Test coverage report  


Generating the TCK audit coverage report
----------------------------------------
Run the following command to generate the TCK audit report:

  mvn clean install -Dtck-audit
