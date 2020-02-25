Weld Porting Package - Libraries
================================

Run the build to copy all Weld Porting Package dependencies into /target/dependency dir.

Specify the version of Weld and CDI TCK explicitly.

> mvn clean package -Dweld.version=2.0.0.Beta2 -Dcdi.tck.version=1.1.0.Beta2