package org.jboss.cdi.tck.tests.build.compatible.extensions.customInterceptorBinding;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@SpecVersion(spec = "cdi", version = "4.0")
public class CustomInterceptorBindingTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(CustomInterceptorBindingTest.class)
                .withBuildCompatibleExtension(CustomInterceptorBindingExtension.class)
                .build();
    }

    @Test
    //@SpecAssertion(section = TODO, id = "TODO")
    public void test() {
        assertEquals("Intercepted: Hello!", getContextualReference(MyService.class).hello());
    }
}
