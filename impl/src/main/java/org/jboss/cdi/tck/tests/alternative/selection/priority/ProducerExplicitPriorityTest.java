package org.jboss.cdi.tck.tests.alternative.selection.priority;

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_APPLICATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "4.1")
public class ProducerExplicitPriorityTest extends AbstractTest {

    public static final String DEFAULT = "default";
    public static final String ALT = "alternative";
    public static final String ALT2 = "alternative2";

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProducerExplicitPriorityTest.class).build();
    }

    @Inject
    @ProducedByMethod
    Alpha alphaMethodProducer;

    @Inject
    @ProducedByField
    Alpha alphaFieldProducer;

    @Inject
    @ProducedByMethod
    Beta betaMethodProducer;

    @Inject
    @ProducedByField
    Beta betaFieldProducer;

    @Inject
    @ProducedByMethod
    Gamma gammaMethodProducer;

    @Inject
    @ProducedByField
    Gamma gammaFieldProducer;

    @Inject
    @ProducedByMethod
    Delta deltaMethodProducer;

    @Inject
    @ProducedByField
    Delta deltaFieldProducer;


    @Test
    @SpecAssertions({@SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_APPLICATION, id = "ca"),
            @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_APPLICATION, id = "cb")})
    public void testAlternativeProducerWithPriority() {
        assertNotNull(alphaMethodProducer);
        assertNotNull(alphaFieldProducer);

        assertEquals(alphaMethodProducer.ping(), ALT);
        assertEquals(alphaFieldProducer.ping(), ALT);
    }

    @Test
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_APPLICATION, id = "dd")
    public void testPriorityOnProducerOverPriorityOnClass() {
        assertNotNull(betaMethodProducer);
        assertNotNull(betaFieldProducer);
        assertNotNull(gammaFieldProducer);
        assertNotNull(gammaMethodProducer);
        assertNotNull(deltaFieldProducer);
        assertNotNull(deltaMethodProducer);

        assertEquals(betaMethodProducer.ping(), ALT2);
        assertEquals(betaFieldProducer.ping(), ALT2);
        assertEquals(gammaFieldProducer.ping(), ALT2);
        assertEquals(gammaMethodProducer.ping(), ALT2);
        assertEquals(deltaFieldProducer.ping(), ALT2);
        assertEquals(deltaMethodProducer.ping(), ALT2);
    }
}
