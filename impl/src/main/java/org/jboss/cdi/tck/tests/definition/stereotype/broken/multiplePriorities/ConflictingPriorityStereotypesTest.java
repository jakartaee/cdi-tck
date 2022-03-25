package org.jboss.cdi.tck.tests.definition.stereotype.broken.multiplePriorities;

import jakarta.enterprise.inject.spi.DefinitionException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.definition.stereotype.priority.PriorityStereotype;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "4.0")
public class ConflictingPriorityStereotypesTest extends AbstractTest {

    @Deployment
    @ShouldThrowException(DefinitionException.class)
    public static WebArchive deploy() {
        return new WebArchiveBuilder().withTestClass(ConflictingPriorityStereotypesTest.class)
                .withClasses(PriorityStereotype.class, SomeBean.class, PriorityStereotype2.class)
                .withBeansXml(new BeansXml())
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.DECLARING_STEREOTYPE_WITH_PRIORITY, id = "b", note = "direct")
    public void testConflictingPrioritiesFromStereotypes() {
        // test should throw an exception
    }
}
