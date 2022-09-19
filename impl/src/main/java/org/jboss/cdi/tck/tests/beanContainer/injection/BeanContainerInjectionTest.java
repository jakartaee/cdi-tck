package org.jboss.cdi.tck.tests.beanContainer.injection;

import static org.jboss.cdi.tck.cdi.Sections.BEANCONTAINER;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.BeanContainer;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Simple test which verifies that we can provide {@link BeanContainer} as a built-in bean
 */
public class BeanContainerInjectionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeanContainerInjectionTest.class)
                .withClasses(MyBean.class).build();
    }

    @Inject
    MyBean myBean;

    @Inject
    Instance<Object> instance;

    @Test
    @SpecAssertions({ @SpecAssertion(section = BEANCONTAINER, id = "aa"),
            @SpecAssertion(section = BEANCONTAINER, id = "ab"),
            @SpecAssertion(section = BEANCONTAINER, id = "ac") })
    public void testInjectionOfBeanContainerType() {
        // bean injection; use the container for arbitrary action
        myBean.getBeanContainer().isNormalScope(ApplicationScoped.class);

        // dynamic resolution, verify type, explicit qualifier and test scope
        Instance<BeanContainer> beanContainerInstance = instance.select(BeanContainer.class, Default.Literal.INSTANCE);
        Assert.assertTrue(beanContainerInstance.isResolvable());
        Assert.assertEquals(beanContainerInstance.getHandle().getBean().getScope(), Dependent.class);
    }
}
