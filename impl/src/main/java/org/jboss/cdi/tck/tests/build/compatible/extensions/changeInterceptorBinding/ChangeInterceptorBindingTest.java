package org.jboss.cdi.tck.tests.build.compatible.extensions.changeInterceptorBinding;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.interceptors.InterceptorsSections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test interceptor bindings applied to methods via extension are used to bind the correct interceptor and returned from InvocationContext.getInterceptorBindings()
 */
@SpecVersion(spec = "cdi", version = "4.1")
public class ChangeInterceptorBindingTest extends AbstractTest {
    
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(ChangeInterceptorBindingTest.class)
                .withBuildCompatibleExtension(ChangeInterceptorBindingExtension.class)
                .build();
    }
    
    @Test
    @SpecAssertion(section = Sections.ENHANCEMENT_PHASE, id = "b")
    @SpecAssertion(section = InterceptorsSections.INVOCATIONCONTEXT, id="o")
    public void test() {
        assertEquals(getContextualReference(MyService.class).hello(), "Intercepted(foo): Hello!");
    }

}
