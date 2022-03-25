package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanWithLookup;

import jakarta.enterprise.inject.Instance;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@SpecVersion(spec = "cdi", version = "4.0")
public class SyntheticBeanWithLookupTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        // no beans.xml + an extension = not a bean archive, bean classes are added through the extension
        return new WebArchiveBuilder()
                .withTestClassPackage(SyntheticBeanWithLookupTest.class)
                .withBuildCompatibleExtension(SyntheticBeanWithLookupExtension.class)
                .withoutBeansXml()
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.DISCOVERY_PHASE, id = "c")
    @SpecAssertion(section = Sections.SYNTHESIS_PHASE, id = "a")
    public void test() {
        Instance<Object> lookup = getCurrentBeanContainer().createInstance();

        assertEquals(0, MyPojo.createdCounter.get());
        assertEquals(0, MyPojo.destroyedCounter.get());
        assertEquals(0, MyPojoCreator.counter.get());
        assertEquals(0, MyPojoDisposer.counter.get());
        assertEquals(0, MyDependentBean.createdCounter.get());
        assertEquals(0, MyDependentBean.destroyedCounter.get());

        Instance.Handle<MyPojo> bean = lookup.select(MyPojo.class).getHandle();
        assertEquals("Hello!", bean.get().hello());

        assertEquals(1, MyPojo.createdCounter.get());
        assertEquals(0, MyPojo.destroyedCounter.get());
        assertEquals(1, MyPojoCreator.counter.get());
        assertEquals(0, MyPojoDisposer.counter.get());
        assertEquals(1, MyDependentBean.createdCounter.get());
        assertEquals(0, MyDependentBean.destroyedCounter.get());

        bean.destroy();

        assertEquals(1, MyPojo.createdCounter.get());
        assertEquals(1, MyPojo.destroyedCounter.get());
        assertEquals(1, MyPojoCreator.counter.get());
        assertEquals(1, MyPojoDisposer.counter.get());
        assertEquals(2, MyDependentBean.createdCounter.get());
        assertEquals(2, MyDependentBean.destroyedCounter.get());
    }
}
