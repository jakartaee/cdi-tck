package org.jboss.cdi.tck.tests.full.lookup.injectionpoint;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.INJECTION_POINT;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.spi.Decorator;
import jakarta.enterprise.inject.spi.InjectionPoint;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class InjectionPointTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InjectionPointTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL).decorators(AnimalDecorator1.class, AnimalDecorator2.class, AnimalDecorator3.class))
                .build();
    }

    /**
     * CDI-78 reopened.
     * <p>
     * Base of this test was originally part of CDITCK-138 but disappeared during branch merge.
     * </p>
     */
    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "dba")
    public void testIsDelegate() {

        assert !getContextualReference(FieldInjectionPointBean.class).getInjectedBean().getInjectedMetadata().isDelegate();

        Cat cat = getContextualReference(Cattery.class).getCat();
        // Cat is decorated
        assert cat.hello().equals("hello!!!");
        assert cat.getBeanManager() != null;
        assert cat.getInjectionPoint() != null;
        assert !cat.getInjectionPoint().isDelegate();

        List<Decorator<?>> animalDecorators = getCurrentManager().resolveDecorators(Collections.<Type>singleton(Animal.class));
        assert animalDecorators.size() == 3;
        for (Decorator<?> animalDecorator : animalDecorators) {
            // Decorator has two injection points - metadata and delegate
            assert animalDecorator.getInjectionPoints().size() == 2;

            for (InjectionPoint injectionPoint : animalDecorator.getInjectionPoints()) {
                if (injectionPoint.getType().equals(InjectionPoint.class)) {
                    assertFalse(injectionPoint.isDelegate());
                } else if (injectionPoint.getType().equals(Animal.class)) {
                    assertTrue(injectionPoint.isDelegate());
                } else if (injectionPoint.getType().equals(Toy.class)) {
                    assertFalse(injectionPoint.isDelegate());
                } else {
                    // Unexpected injection point type
                    assert false;
                }
            }
        }
        Toy toy = cat.getToy();
        assert toy.getInjectionPoint() != null;
        assert toy.getInjectionPoint().getBean().getBeanClass().equals(AnimalDecorator2.class);
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "eb")
    public void testPassivationCapability() throws Exception {
        InjectionPoint ip1 = getContextualReference(FieldInjectionPointBean.class).getInjectedBean().getInjectedMetadata();
        InjectionPoint ip2 = getContextualReference(MethodInjectionPointBean.class).getInjectedBean().getInjectedMetadata();
        InjectionPoint ip3 = getContextualReference(ConstructorInjectionPointBean.class).getInjectedBean().getInjectedMetadata();

        ip1 = (InjectionPoint) activate(passivate(ip1));
        ip2 = (InjectionPoint) activate(passivate(ip2));
        ip3 = (InjectionPoint) activate(passivate(ip3));

        assert ip1.getType().equals(BeanWithInjectionPointMetadata.class);
        assert ip2.getType().equals(BeanWithInjectionPointMetadata.class);
        assert ip3.getType().equals(BeanWithInjectionPointMetadata.class);
    }
}
