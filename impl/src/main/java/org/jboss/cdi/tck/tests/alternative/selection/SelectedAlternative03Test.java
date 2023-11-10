package org.jboss.cdi.tck.tests.alternative.selection;

import static org.jboss.cdi.tck.cdi.Sections.UNSATISFIED_AND_AMBIG_DEPENDENCIES;

import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "4.1")
public class SelectedAlternative03Test extends AbstractTest {

    public static final String DEFAULT = "default";
    public static final String ALT1 = "alt1";
    public static final String ALT2 = "alt2";

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(SelectedAlternative03Test.class)
                .withClasses(Delta.class, StandardDeltaProducer.class, AlternativeDeltaProducer1.class,
                        AlternativeDeltaProducer2.class).build();
    }

    @Inject
    Delta delta;

    @Test
    @SpecAssertion(section = UNSATISFIED_AND_AMBIG_DEPENDENCIES, id = "cb")
    public void testMultipleAlternativeBeansWithProducers() {
        Assert.assertNotNull(delta);
        Assert.assertEquals(delta.ping(), ALT2);
    }
}
