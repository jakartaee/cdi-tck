package org.jboss.cdi.tck.tests.build.compatible.extensions.priority;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "4.0")
public class PriorityTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(PriorityTest.class)
                .withBuildCompatibleExtension(PriorityExtension.class)
                .build();
    }

    @Test
    //@SpecAssertion(section = TODO, id = "TODO")
    public void trigger() {
        // test is present in PriorityExtension and if it fails, deployment should fail
    }
}
