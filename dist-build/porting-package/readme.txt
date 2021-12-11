Weld Porting Package - Libraries
================================

Run the build to copy all Weld Porting Package dependencies into /target/dependency dir.

Specify the version of Weld and CDI TCK explicitly.

> mvn clean package -Dweld.version=5.0.0.Alpha2 -Dcdi.tck.version=4.0.0-RC1