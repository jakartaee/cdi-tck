package org.jboss.cdi.tck.tests.build.compatible.extensions.customStereotype;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

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
    //@SpecAssertion(section = TODO, id = "TODO")
    public void test() {
        assertEquals(ApplicationScoped.class, getUniqueBean(MyService.class).getScope());
        assertEquals("Hello!", getContextualReference(MyService.class).hello());
    }
}
