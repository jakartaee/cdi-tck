package org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes.synthetic.interceptor;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.enterprise.inject.spi.InterceptionType;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * First register two synthetic interceptor beans (AfterBeanDiscovery) and then veto one of them (ProcessBeanAttributes).
 * 
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class SyntheticInterceptorVetoedTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SyntheticInterceptorVetoedTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors()
                                .clazz(ExternalInterceptor.class.getName()).up())
                .withExtension(ExternalInterceptorExtension.class).build();
    }

    @Inject
    ExternalInterceptorExtension extension;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.10", id = "be"), @SpecAssertion(section = "11.5.2", id = "dd"),
            @SpecAssertion(section = "11.5.6", id = "bc") })
    public void testSyntheticInterceptorBeanCanBeVetoed() {
        assertTrue(extension.isTypeVetoed());
        assertTrue(extension.isBeanRegistered());
        assertTrue(extension.isBeanVetoed());
        // verify that one of these is vetoed (we do not know which one)
        int fooInterceptors = getCurrentManager().resolveInterceptors(InterceptionType.AROUND_INVOKE,
                FooBinding.Literal.INSTANCE).size();
        int barInterceptors = getCurrentManager().resolveInterceptors(InterceptionType.AROUND_INVOKE,
                BarBinding.Literal.INSTANCE).size();
        assertEquals(fooInterceptors + barInterceptors, 1);
    }

}