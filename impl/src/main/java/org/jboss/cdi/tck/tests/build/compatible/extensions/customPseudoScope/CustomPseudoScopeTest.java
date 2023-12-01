package org.jboss.cdi.tck.tests.build.compatible.extensions.customPseudoScope;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

@SpecVersion(spec = "cdi", version = "4.0")
public class CustomPseudoScopeTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(CustomPseudoScopeTest.class)
                .withBuildCompatibleExtension(CustomPseudoScopeExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.DISCOVERY_PHASE, id = "a", note = "Register custom pseudo-scope")
    public void test() {
        PrototypeBean prototypeBean = getContextualReference(PrototypeBean.class);

        assertNotEquals(getContextualReference(PrototypeBean.class).getId(), prototypeBean.getId());

        ApplicationScopedBean applicationScopedBean = getContextualReference(ApplicationScopedBean.class);
        assertEquals(getContextualReference(ApplicationScopedBean.class).getPrototypeId(),
                applicationScopedBean.getPrototypeId());
        assertNotEquals(prototypeBean.getId(), applicationScopedBean.getPrototypeId());

        RequestScopedBean requestScopedBean = getContextualReference(RequestScopedBean.class);
        assertEquals(getContextualReference(RequestScopedBean.class).getPrototypeId(),
                requestScopedBean.getPrototypeId());
        assertNotEquals(prototypeBean.getId(), requestScopedBean.getPrototypeId());

        DependentBean dependentBean = getContextualReference(DependentBean.class);
        assertNotEquals(getContextualReference(DependentBean.class).getPrototypeId(),
                dependentBean.getPrototypeId());
        assertNotEquals(prototypeBean.getId(), dependentBean.getPrototypeId());
    }
}
