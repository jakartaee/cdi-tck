package org.jboss.cdi.tck.tests.definition.stereotype.priority.inherited;

import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.testng.annotations.Test;

public class StereotypeInheritedPriorityTest extends AbstractTest {

    @Deployment
    public static WebArchive deploy() {
        return new WebArchiveBuilder().withTestClassPackage(StereotypeInheritedPriorityTest.class)
                .withBeansXml(new BeansXml())
                .build();
    }

    @Inject
    BeanManager bm;

    @Test
    public void testPriorityWasInherited() {
        // if the inheritance works, FooAlternative will inherit the priority and therefore will be enabled
        Instance<FooAlternative> fooInstance = bm.createInstance().select(FooAlternative.class);
        assertTrue(fooInstance.isResolvable());
    }
}
