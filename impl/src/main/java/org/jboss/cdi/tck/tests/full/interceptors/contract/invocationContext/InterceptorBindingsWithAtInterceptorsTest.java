package org.jboss.cdi.tck.tests.full.interceptors.contract.invocationContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INVOCATIONCONTEXT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@SpecVersion(spec = "interceptors", version = "2.2")
@Test(groups = CDI_FULL)
public class InterceptorBindingsWithAtInterceptorsTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InterceptorBindingsWithAtInterceptorsTest.class).build();
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "n")
    public void testInterceptorBindingsEmptyWithAtInterceptors() {
        Dog dog = getContextualReference(Dog.class);
        assertEquals(dog.foo(), "dog: bar");
        assertNotNull(DogInterceptor.getAllBindings());
        assertTrue(DogInterceptor.getAllBindings().isEmpty());

        Fish fish = getContextualReference(Fish.class);
        assertEquals(fish.foo(), "fish: bar");
        assertNotNull(FishInterceptor.getAllBindings());
        assertTrue(FishInterceptor.getAllBindings().isEmpty());
    }
}
