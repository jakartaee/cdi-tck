package org.jboss.cdi.tck.test.spi.broken.warsp;

import jakarta.enterprise.inject.spi.DeploymentException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.cdi.tck.impl.ConfigurationFactory;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

/**
 * Test that a CDI-lite mode archive triggers the SourceProcessor and VersionProcessor annotation processor
 * for a WebArchive based test that generates a DeploymentException.
 */
public class BrokenSourceProcessorTest extends Arquillian {
    @ShouldThrowException(DeploymentException.class)
    @Deployment
    public static WebArchive createTestArchive() {
        System.setProperty("org.jboss.cdi.tck.test.spi.container.TckAnnotationProcessor.ran", "false");
        ConfigurationFactory.get().setCDILiteModeFlag(Boolean.TRUE);
        System.out.printf("SourceProcessorTest.createTestArchive()\n");
        WebArchive war = new WebArchiveBuilder()
                .debugMode()
                .withTestClassPackage(BrokenSourceProcessorTest.class)
                .withClass(org.jboss.cdi.tck.test.spi.sp.VersionInfo.class)
                .build();
        return war;
    }

    @Test
    public void testDeploymentEx() {
    }
}
