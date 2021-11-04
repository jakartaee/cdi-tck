package org.jboss.cdi.tck.tests.definition.stereotype.broken.multiplePriorities;

import jakarta.enterprise.inject.spi.DefinitionException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.definition.stereotype.priority.PriorityStereotype;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "4.0")
public class ConflictingPriorityStereotypesTest extends AbstractTest {

    @Deployment
    @ShouldThrowException(DefinitionException.class)
    public static WebArchive deploy() {
        return new WebArchiveBuilder().withTestClassPackage(ConflictingPriorityStereotypesTest.class)
                .withClass(PriorityStereotype.class)
                .withBeansXml(new BeansXml())
                .build();
    }

    @Test
    //@SpecAssertion(section = "TODO", id = "TODO")
    public void testConflictingPrioritiesFromStereotypes() {
        // test should throw an exception
    }
}
