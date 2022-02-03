package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import static org.testng.Assert.assertEquals;

import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "4.0")
public class StereotypeWithPriorityTest extends AbstractTest {

    @Deployment
    public static WebArchive deploy() {
        return new WebArchiveBuilder().withTestClassPackage(StereotypeWithPriorityTest.class)
                .withBeansXml(new BeansXml())
                .build();
    }

    @Inject
    Foo foo;

    @Inject
    Bar bar;

    @Inject
    Baz baz;

    @Inject
    Charlie charlie;

    @Test
    //@SpecAssertion(section = "TODO", id = "TODO")
    public void testStereotypeWithPriority() {
        // injected Foo should be FooAlternative
        assertEquals(foo.ping(), FooAlternative.class.getSimpleName());
    }

    @Test
    //@SpecAssertion(section = "TODO", id = "TODO")
    public void testStereotypeWithAlternativeAndPriority() {
        // injected Bar should be instance of BarExtended
        assertEquals(bar.ping(), BarExtended.class.getSimpleName());
    }

    @Test
    //@SpecAssertion(section = "TODO", id = "TODO")
    public void testBeanPriorityFromStereotypeOverridesOtherAlternative() {
        // injected Baz should be instance of BazAlternative2
        assertEquals(baz.ping(), BazAlternative2.class.getSimpleName());
    }

    @Test
    //@SpecAssertion(section = "TODO", id = "TODO")
    public void testBeanOverridesPriorityFromStereotype() {
        // injected Charlie should be instance of CharlieAlternative
        assertEquals(charlie.ping(), CharlieAlternative.class.getSimpleName());
    }
}
