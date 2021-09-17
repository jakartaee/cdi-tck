package org.jboss.cdi.tck.tests.deployment.discovery;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

/**
 * Tests that a singular archive with empty beans.xml results in annotated discovery mode.
 */
public class EmptyBeansXmlDiscoveryTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class).addClasses(SomeAnnotatedBean.class, SomeUnannotatedBean.class)
                .addAsWebInfResource(new StringAsset(""), "beans.xml");
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
