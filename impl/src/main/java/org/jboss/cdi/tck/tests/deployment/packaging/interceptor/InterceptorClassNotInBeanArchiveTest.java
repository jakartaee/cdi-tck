package org.jboss.cdi.tck.tests.deployment.packaging.interceptor;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * TODO add assertions as soon as WELD-1073 is recognized as a bug
 */
public class InterceptorClassNotInBeanArchiveTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        // Foo -> BDA, BarBinding and BarInterceptor -> non-BDA
        return new WebArchiveBuilder()
                .withTestClass(InterceptorClassNotInBeanArchiveTest.class)
                .withBeanLibrary(
                        Descriptors.create(BeansDescriptor.class).createInterceptors().clazz(BarInterceptor.class.getName())
                                .up(), Foo.class).withLibrary(BarBinding.class).withLibrary(BarInterceptor.class).build();
    }

    @Inject
    Foo foo;

    @Test
    public void testInterceptor() {
        foo.ping();
        Assert.assertTrue(BarInterceptor.intercepted);
    }

}
