package org.jboss.cdi.tck.test.spi.container;

import java.io.File;
import java.util.Set;

import javax.tools.DiagnosticCollector;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.container.LifecycleException;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.cdi.tck.shrinkwrap.ArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.ExceptionAsset;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.classloader.ShrinkWrapClassLoader;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptor;

/**
 * Largely empty DeployableContainer implementation that only validates that the test archive
 * type seen by the deploy method is a JavaArchive. This is used in conjunction with the SourceProcessor
 * tests.
 *
 * This container does illustrate the need to check for deployment exceptions generated during the annotation processor
 * phase when the {@link org.jboss.cdi.tck.spi.SourceProcessor#process(Set, File, DiagnosticCollector)} is called.
 */
public class MockContainer implements DeployableContainer<MockConfiguration> {
    @Override
    public Class<MockConfiguration> getConfigurationClass() {
        return MockConfiguration.class;
    }

    @Override
    public void setup(MockConfiguration configuration) {

    }

    @Override
    public void start() throws LifecycleException {
        System.out.println("MockContainer.start");
    }

    @Override
    public void stop() throws LifecycleException {
        System.out.println("MockContainer.stop");
    }

    @Override
    public ProtocolDescription getDefaultProtocol() {
        return new ProtocolDescription("Local");
    }

    @Override
    public ProtocolMetaData deploy(Archive<?> archive) throws DeploymentException {
        System.out.printf("MockContainer.deploy, %s\n", archive.getName());
        // Handle exceptions generated in the PHASE_ONE source processor run
        if(archive.contains(ArchiveBuilder.SP_EXCEPTION)) {
            Node exNode = archive.get(ArchiveBuilder.SP_EXCEPTION);
            ExceptionAsset exceptionAsset = (ExceptionAsset) exNode.getAsset();
            Exception ex = exceptionAsset.getSource();
            DeploymentException dex;
            if(ex instanceof DeploymentException) {
                dex = (DeploymentException) ex;
            } else {
                dex = new DeploymentException("SourceProcessor failure", ex);
            }
            throw dex;
        }
        // TODO, deployment classloader to validate generated classes?
        ShrinkWrapClassLoader classLoader = new ShrinkWrapClassLoader(archive.getClass().getClassLoader(), archive);
        return new ProtocolMetaData();
    }

    @Override
    public void undeploy(Archive<?> archive) throws DeploymentException {
        System.out.printf("MockContainer.undeploy, %s\n", archive.getName());
    }

    @Override
    public void deploy(Descriptor descriptor) throws DeploymentException {
        throw new UnsupportedOperationException("MockContainer does not support deployment of Descriptors");

    }

    @Override
    public void undeploy(Descriptor descriptor) throws DeploymentException {
        throw new UnsupportedOperationException("MockContainer does not support deployment of Descriptors");

    }

}
