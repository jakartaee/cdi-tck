package org.jboss.cdi.tck.tests.definition.stereotype.broken.multiplePriorities.inherited;

import jakarta.enterprise.inject.spi.DefinitionException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.definition.stereotype.priority.inherited.DumbStereotype;
import org.jboss.cdi.tck.tests.definition.stereotype.priority.inherited.StereotypeWithPriority;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.testng.annotations.Test;

public class StereotypeInheritedPriorityConflictTest extends AbstractTest {

    @Deployment
    @ShouldThrowException(DefinitionException.class)
    public static WebArchive deploy() {
        return new WebArchiveBuilder().withTestClassPackage(StereotypeInheritedPriorityConflictTest.class)
                .withClasses(DumbStereotype.class, StereotypeWithPriority.class)
                .withBeansXml(new BeansXml())
                .build();
    }

    @Test
    public void testInheritedStereotypesAreConflicting() {
        // test should throw an exception
    }
}
