package org.jboss.jsr299.tck.tests.implementation.simple.definition;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class EnterpriseBeanNotDiscoveredAsManagedBeanTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseBeanNotDiscoveredAsManagedBeanTest.class)
                .withExtension("javax.enterprise.inject.spi.Extension")
                // Originally with faces-config.xml however this resource does not exist in
                // /jsr299-tck-impl/src/main/resources/org/jboss/jsr299/tck/tests/implementation/simple/definition
                // .withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml")
                .build();
    }

    @Test
    @SpecAssertion(section = "3.1.1", id = "f")
    public void testClassesImplementingEnterpriseBeanInterfaceNotDiscoveredAsSimpleBean() {
        assert !EnterpriseBeanObserver.observedEnterpriseBean;
        assert EnterpriseBeanObserver.observedAnotherBean;
    }

}
