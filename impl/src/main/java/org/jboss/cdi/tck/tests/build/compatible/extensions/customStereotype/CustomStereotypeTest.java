package org.jboss.cdi.tck.tests.build.compatible.extensions.customStereotype;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@SpecVersion(spec = "cdi", version = "4.0")
public class CustomStereotypeTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        // no beans.xml + an extension = not a bean archive, bean classes are added through the extension
        return new WebArchiveBuilder()
                .withTestClassPackage(CustomStereotypeTest.class)
                .withBuildCompatibleExtension(CustomStereotypeExtension.class)
                .withoutBeansXml()
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.DISCOVERY_PHASE, id = "a", note = "MyCustomStereotype.value has @Stereotype")
    @SpecAssertion(section = Sections.DISCOVERY_PHASE, id = "b", note = "NotDiscoveredBean not in scanned classes")
    public void test() {
        assertEquals(ApplicationScoped.class, getUniqueBean(MyService.class).getScope());
        assertEquals("Hello!", getContextualReference(MyService.class).hello());

        assertTrue(getBeans(NotDiscoveredBean.class).isEmpty());
    }
}
