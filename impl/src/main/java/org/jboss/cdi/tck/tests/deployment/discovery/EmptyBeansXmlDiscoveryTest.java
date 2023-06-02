package org.jboss.cdi.tck.tests.deployment.discovery;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests that a singular archive with empty beans.xml results in annotated discovery mode.
 */
@SpecVersion(spec = "cdi", version = "4.0")
public class EmptyBeansXmlDiscoveryTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EmptyBeansXmlDiscoveryTest.class)
                .withBeansXml("beans.xml").build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    public void testBeanArchiveWithEmptyBeansXml() {
        Instance<Object> instance = CDI.current().getBeanContainer().createInstance();
        Instance<SomeAnnotatedBean> annotatedBeanInstance = instance.select(SomeAnnotatedBean.class);
        assertTrue(annotatedBeanInstance.isResolvable());
        annotatedBeanInstance.get().pong();
        Instance<SomeUnannotatedBean> unannotatedBeanInstance = instance.select(SomeUnannotatedBean.class);
        assertFalse(unannotatedBeanInstance.isResolvable());
    }
}
