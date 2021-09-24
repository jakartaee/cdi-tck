package org.jboss.cdi.tck.tests.full.implementation.producer.field.lifecycle;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.SPECIALIZATION;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class ProducerFieldLifecycleTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProducerFieldLifecycleTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SPECIALIZATION, id = "cd") })
    public void testProducerFieldFromSpecializingBeanUsed() throws Exception {
        TarantulaConsumer spiderConsumer = getContextualReference(TarantulaConsumer.class);
        assert spiderConsumer.getConsumedTarantula() != null;
        assert spiderConsumer.getConsumedTarantula() instanceof DefangedTarantula;
    }
}
