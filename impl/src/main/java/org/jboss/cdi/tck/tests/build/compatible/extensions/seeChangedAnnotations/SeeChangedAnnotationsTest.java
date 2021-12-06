package org.jboss.cdi.tck.tests.build.compatible.extensions.seeChangedAnnotations;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "4.0")
public class SeeChangedAnnotationsTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SeeChangedAnnotationsTest.class)
                .withBuildCompatibleExtension(SeeChangedAnnotationsExtension.class)
                .build();
    }

    @Test
    //@SpecAssertion(section = TODO, id = "TODO")
    public void test() {
        // test is present in SeeChangedAnnotationsExtension and if it fails, deployment should fail
    }
}
