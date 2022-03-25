package org.jboss.cdi.tck.tests.build.compatible.extensions.changeInjectionPoint;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@SpecVersion(spec = "cdi", version = "4.0")
public class ChangeInjectionPointTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(ChangeInjectionPointTest.class)
                .withBuildCompatibleExtension(ChangeInjectionPointExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.ENHANCEMENT_PHASE, id = "b", note = "MyOtherService.myService with @MyQualifier matches MyServiceBar")
    public void test() {
        MyOtherService bean = getContextualReference(MyOtherService.class);
        // all beans are dependent, so there's no client proxy and direct field access and `instanceof` are OK
        assertTrue(bean.myService instanceof MyServiceBar);
    }
}
