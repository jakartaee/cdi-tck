package org.jboss.cdi.tck.tests.build.compatible.extensions.changeBeanQualifier;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@SpecVersion(spec = "cdi", version = "4.0")
public class ChangeBeanQualifierTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(ChangeBeanQualifierTest.class)
                .withBuildCompatibleExtension(ChangeBeanQualifierExtension.class)
                .build();
    }

    @Test
    //@SpecAssertion(section = TODO, id = "TODO")
    public void test() {
        MyOtherService bean = getContextualReference(MyOtherService.class);
        // all beans are dependent, so there's no client proxy and direct field access and `instanceof` are OK
        assertTrue(bean.myService instanceof MyServiceBar);
    }
}
