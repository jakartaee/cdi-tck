package org.jboss.cdi.tck.tests.build.compatible.extensions.validation;

import jakarta.enterprise.inject.spi.DeploymentException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "4.0")
public class ValidationTest extends AbstractTest {
    @Deployment
    @ShouldThrowException(DeploymentException.class)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(ValidationTest.class)
                .withBuildCompatibleExtension(ValidationExtension.class)
                .build();
    }

    @Test
    //@SpecAssertion(section = TODO, id = "TODO")
    public void trigger() {
        // deployment should fail
    }
}
