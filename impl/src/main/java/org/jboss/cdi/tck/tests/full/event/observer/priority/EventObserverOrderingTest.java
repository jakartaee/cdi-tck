package org.jboss.cdi.tck.tests.full.event.observer.priority;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_METHOD;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_ORDERING;
import static org.testng.Assert.assertEquals;

import jakarta.enterprise.inject.spi.ObserverMethod;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class EventObserverOrderingTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withTestClassPackage(EventObserverOrderingTest.class)
                .withExtension(ObserverExtension.class).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_ORDERING, id = "a"),  @SpecAssertion(section = OBSERVER_METHOD, id = "ea")  })
    public void testDefaultPriority(ObserverExtension observerExtension) {
        assertEquals(observerExtension.getObserverMethodPriority("Observer2.observeMoon").intValue(), ObserverMethod.DEFAULT_PRIORITY);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_METHOD, id = "ea") })
    public void testProcessObserverMethodPriority(ObserverExtension observerExtension) {
        assertEquals(observerExtension.getObserverMethodPriority("Observer3.observeMoon").intValue(), ObserverMethod.DEFAULT_PRIORITY + 400);
    }
}
