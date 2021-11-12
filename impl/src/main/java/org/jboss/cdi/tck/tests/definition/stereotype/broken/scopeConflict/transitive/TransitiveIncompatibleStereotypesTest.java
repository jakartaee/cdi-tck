package org.jboss.cdi.tck.tests.definition.stereotype.broken.scopeConflict.transitive;

import jakarta.enterprise.inject.spi.DefinitionException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.cdi.Sections.DEFAULT_SCOPE;

@SpecVersion(spec = "cdi", version = "2.0")
public class TransitiveIncompatibleStereotypesTest extends AbstractTest {

    @ShouldThrowException(DefinitionException.class)
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(TransitiveIncompatibleStereotypesTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DEFAULT_SCOPE, id = "da") })
    public void testMultipleTransitiveIncompatibleScopeStereotypes() {
    }
}
