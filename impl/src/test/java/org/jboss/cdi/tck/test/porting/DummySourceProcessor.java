package org.jboss.cdi.tck.test.porting;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import jakarta.enterprise.inject.spi.DeploymentException;
import org.jboss.cdi.tck.spi.SourceProcessor;
import org.jboss.cdi.tck.test.spi.container.TckAnnotationProcessor;

/**
 * Implementation of SourceProcessor that invokes the JavaCompiler to process the test archive source. It also includes
 * a hardcoded reference to the {@link TckAnnotationProcessor} to validate that annotation processors can run on the
 * test archive.
 */
public class DummySourceProcessor implements SourceProcessor {
    private static DummySourceProcessor instance;

    public static DummySourceProcessor getInstance() {
        return instance;
    }

    public DummySourceProcessor() {
        DummySourceProcessor.instance = this;
    }

    @Override
    public void process(Set<File> testSources, File outputDir, DiagnosticCollector<JavaFileObject> errors) {
        System.out.printf("DummySourceProcessor.process, [%s];\n\toutputDir: %s\n", testSources, outputDir.getAbsolutePath());
        if(testSources.size() > 0) {
            doCompile(testSources, outputDir, errors);
        }
    }

    private void doCompile(Collection<File> testSources, File outputDir, DiagnosticCollector<JavaFileObject> errors) {
        System.out.printf("Begin DummySourceProcessor.doCompile, %s\n", testSources);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager mgr = compiler.getStandardFileManager(errors, null, null );
        try(mgr) {
            String[] options = {"-d", outputDir.getAbsolutePath(), "-verbose"};
            Iterable<? extends JavaFileObject> sources = mgr.getJavaFileObjectsFromFiles(testSources);
            JavaCompiler.CompilationTask task = compiler.getTask( null, mgr, errors, Arrays.asList(options), null, sources );
            ArrayList<Processor> processors = new ArrayList<>();
            processors.add(new TckAnnotationProcessor());
            task.setProcessors(processors);
            Boolean success = task.call();
            System.out.printf("JavaCompiler.success=%s, info=%s\n", success, errors.getDiagnostics());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.printf("End DummySourceProcessor.doCompile\n");
        if(!errors.getDiagnostics().isEmpty()) {
            // Ignore warnings such as the following when run on JVM versions post 11
            // [warning: Supported source version 'RELEASE_11' from annotation processor 'org.jboss.cdi.tck.test.spi.container.TckAnnotationProcessor' less than -source '17']
            for(Diagnostic diag : errors.getDiagnostics()) {
                if(diag.getKind() == Diagnostic.Kind.ERROR) {
                    String msg = errors.getDiagnostics().toString();
                    throw new DeploymentException(msg);
                }
            }
        }
    }
}
