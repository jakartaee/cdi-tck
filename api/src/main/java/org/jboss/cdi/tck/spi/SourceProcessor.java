package org.jboss.cdi.tck.spi;

import java.io.File;
import java.util.Set;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 * This interface provides operations relating to build time processing of the test archive source.
 *
 * The TCK porting package must provide an implementation of this interface which is suitable for the target implementation.
 *
 */
public interface SourceProcessor {
    public static final String PROPERTY_NAME = SourceProcessor.class.getName();

    /**
     * Called to process the test archive source file. This is called after the base test classes have been added to
     * the test archive. Classes generated in this phase will be added to the test archive to augment or replace
     * existing archive classes.
     *
     * @param testSources - the test source files associated with test archive
     * @param outputDir - the test archive specific directory for any compiler output. Classes found in this directory
     *                  will be added to the test archive to either replace or augment existing classes
     * @param errors - the errors generated during the compliation phase. Any errors will be mapped to CDI deployment
     *               exceptions and propgated to the unit test
     */
    public void process(Set<File> testSources, File outputDir, DiagnosticCollector<JavaFileObject> errors);

}
