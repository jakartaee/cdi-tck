package org.jboss.cdi.tck.test.spi.container;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.container.LifecycleException;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.classloader.ShrinkWrapClassLoader;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptor;

/**
 * Largely empty DeployableContainer implementation that only validates that the test archive
 * type seen by the deploy method is a JavaArchive. This is used in conjunction with the SourceProcessor
 * tests.
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
