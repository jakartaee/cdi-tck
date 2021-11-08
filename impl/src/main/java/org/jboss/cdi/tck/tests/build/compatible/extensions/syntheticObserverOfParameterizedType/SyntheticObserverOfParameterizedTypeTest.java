package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticObserverOfParameterizedType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

@SpecVersion(spec = "cdi", version = "4.0")
public class SyntheticObserverOfParameterizedTypeTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SyntheticObserverOfParameterizedTypeTest.class)
                .withBuildCompatibleExtension(SyntheticObserverOfParameterizedTypeExtension.class)
                .build();
    }

    @Test
    //@SpecAssertion(section = TODO, id = "TODO")
    public void test() {
        MyService myService = getContextualReference(MyService.class);
        myService.fireEvent();

        List<String> expected = List.of("Hello World", "Hello again");
        assertEquals(expected, MyObserver.observed);
    }
}
