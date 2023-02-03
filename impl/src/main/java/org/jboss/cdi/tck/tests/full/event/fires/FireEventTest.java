package org.jboss.cdi.tck.tests.full.event.fires;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.EVENT;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.full.extensions.beanManager.broken.event.ContainerLifecycleEvents;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "4.0")
public class FireEventTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(FireEventTest.class).withClass(ContainerLifecycleEvents.class).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = EVENT, id = "g")
    public void testFireContainerLifecycleEvent(ContainerLifecycleEventDispatcher containerLifecycleEvents) {
        containerLifecycleEvents.fireContainerLifecycleEvents();
    }
}
