package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticObserver;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

@SpecVersion(spec = "cdi", version = "4.0")
public class SyntheticObserverTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SyntheticObserverTest.class)
                .withBuildCompatibleExtension(SyntheticObserverExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.SYNTHESIS_PHASE, id = "b")
    public void test() {
        MyService myService = getContextualReference(MyService.class);
        myService.fireEvent();

        List<String> expected = List.of(
                "Hello World with null", // unqualified event observed by unqualified observer
                "Hello Special with null", // qualified event observed by unqualified observer
                "Hello Special with @MyQualifier" // qualified event observed by qualified observer
        );
        assertEquals(expected, MyObserver.observed);
    }
}
