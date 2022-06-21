# CDI TCK Development


Check out the [TCK Reference Guide](https://eclipse-ee4j.github.io/cdi-tck/) to get acquainted with the CDI TCK and learn how to execute and debug it.

## Building CDI TCK artifacts
To build the CDI TCK artifacts, use:

`mvn -DtckAuditSourceGen install`

or when compiling against staged Jakarta artifacts:

`mvn -DtckAuditSourceGen -Pstaging install`

## Building the CDI TCK distribution
The CDI TCK distribution artifact is built by specifing an additional `-Drelease` property to build the TCK reference
documentation and distribution bundle, e.g.:

`mvn -DtckAuditSourceGen -Drelease install`

## Eclipse Continuous Integration Environment
The Eclipse continuous integration environment interface for the CDI project is located at https://ci.eclipse.org/cdi/
The https://github.com/jakartaee/cdi/wiki/Eclipse-CI-Release-Jobs page describes the jobs found there.

## Sources in GIT

Master branch contains the CDI TCK 4.0

### Source Layout

* .github - the GitHub actions configuration
* api - Api/Spi for vendor integration
* dist-build - assembly project to create the distribution zip
* doc - the TCK user guide source
* docs - the content for the CDI project github pages
* ext-lib - A sample library used by some integration tests
* ide-configs - useful settings for Eclipse and Intellij IDEs
* impl - The core set of tests, excluding those that depend on web and full platform containers
* lang-model - A standalone test suite for the CDI language model; see its [README](./lang-model/README.adoc)
* web - The extra tests that depend on the web profile and full platform
* README.md - this doc
