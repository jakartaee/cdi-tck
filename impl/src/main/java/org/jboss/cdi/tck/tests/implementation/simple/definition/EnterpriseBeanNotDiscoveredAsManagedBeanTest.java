package org.jboss.cdi.tck.tests.implementation.simple.definition;

import static org.jboss.cdi.tck.cdi.Sections.WHAT_CLASSES_ARE_BEANS;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class EnterpriseBeanNotDiscoveredAsManagedBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseBeanNotDiscoveredAsManagedBeanTest.class)
                .withExtension(EnterpriseBeanObserver.class).build();
    }

    @Test
    @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS, id = "f")
    public void testClassesImplementingEnterpriseBeanInterfaceNotDiscoveredAsSimpleBean() {
        assert !EnterpriseBeanObserver.observedEnterpriseBean;
        assert EnterpriseBeanObserver.observedAnotherBean;
    }

}
