package org.jboss.cdi.tck.tests.full.implementation.simple.lifecycle;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_DEPENDENCY;
import static org.jboss.cdi.tck.cdi.Sections.SPECIALIZE_MANAGED_BEAN;

import jakarta.enterprise.inject.Specializes;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.AnnotationLiteral;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;

@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class SimpleBeanLifecycleTest extends AbstractTest {

    private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SimpleBeanLifecycleTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SPECIALIZE_MANAGED_BEAN, id = "ac") })
    public void testSpecializedBeanExtendsManagedBean() {
        assert MountainLion.class.getAnnotation(Specializes.class) != null;
        Bean<Lion> bean = null;
        Bean<Lion> specializedBean = null;
        for (Bean<Lion> lionBean : getBeans(Lion.class, TAME_LITERAL)) {
            if (lionBean.getBeanClass().equals(Lion.class)) {
                bean = lionBean;
            } else if (lionBean.getBeanClass().equals(MountainLion.class)) {
                specializedBean = lionBean;
            }
        }

        assert bean == null;
        assert specializedBean != null;
        assert specializedBean.getBeanClass().getSuperclass().equals(Lion.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY, id = "b") })
    public void testSerializeRequestScoped() throws Exception {
        Cod codInstance = getContextualReference(Cod.class);

        byte[] bytes = passivate(codInstance);
        Object object = activate(bytes);
        codInstance = (Cod) object;
        assert getCurrentConfiguration().getBeans().isProxy(codInstance);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY, id = "b") })
    public void testSerializeSessionScoped() throws Exception {
        Bream instance = getContextualReference(Bream.class);

        byte[] bytes = passivate(instance);
        Object object = activate(bytes);
        instance = (Bream) object;
        assert getCurrentConfiguration().getBeans().isProxy(instance);
    }

}
