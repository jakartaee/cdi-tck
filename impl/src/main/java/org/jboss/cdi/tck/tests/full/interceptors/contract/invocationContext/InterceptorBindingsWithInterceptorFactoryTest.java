package org.jboss.cdi.tck.tests.full.interceptors.contract.invocationContext;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INVOCATIONCONTEXT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.util.Set;

@SpecVersion(spec = "interceptors", version = "2.2")
@Test(groups = CDI_FULL)
public class InterceptorBindingsWithInterceptorFactoryTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InterceptorBindingsWithInterceptorFactoryTest.class).build();
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "n")
    public void testInterceptorBindingsAppliedViaInterceptorFactory() {
        Product product = getContextualReference(Product.class);
        assertEquals(product.ping(), 42);
        assertEquals(product.pong(), 42);

        Set<Annotation> interceptor1Bindings = ProductInterceptor1.getAllBindings();
        assertNotNull(interceptor1Bindings);
        assertEquals(interceptor1Bindings.size(), 2);
        assertTrue(interceptor1Bindings.contains(ProductInterceptorBinding1.Literal.INSTANCE));
        assertTrue(interceptor1Bindings.contains(ProductInterceptorBinding2.Literal.INSTANCE));

        Set<Annotation> interceptor2Bindings = ProductInterceptor2.getAllBindings();
        assertNotNull(interceptor2Bindings);
        assertEquals(interceptor2Bindings.size(), 2);
        assertTrue(interceptor2Bindings.contains(ProductInterceptorBinding1.Literal.INSTANCE));
        assertTrue(interceptor2Bindings.contains(ProductInterceptorBinding3.Literal.INSTANCE));

    }
}
