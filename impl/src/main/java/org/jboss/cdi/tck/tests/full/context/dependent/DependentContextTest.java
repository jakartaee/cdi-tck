package org.jboss.cdi.tck.tests.full.context.dependent;

import java.util.Set;

import jakarta.enterprise.inject.spi.Bean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.TestGroups;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.cdi.Sections.DEPENDENT_CONTEXT_EE;
import static org.jboss.cdi.tck.cdi.Sections.DEPENDENT_DESTRUCTION_EE;

@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = TestGroups.CDI_FULL)
public class DependentContextTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DependentContextTest.class).build();
    }

    @Test
    @SpecAssertion(section = DEPENDENT_CONTEXT_EE, id = "ca")
    public void testInstanceUsedForElEvaluationNotShared() throws Exception {
        Set<Bean<Fox>> foxBeans = getBeans(Fox.class);
        assert foxBeans.size() == 1;

        Fox fox1 = getCurrentConfiguration().getEl().evaluateValueExpression(getCurrentManager(), "#{fox}", Fox.class);
        Fox fox2 = getCurrentConfiguration().getEl().evaluateValueExpression(getCurrentManager(), "#{fox}", Fox.class);
        assert !fox1.equals(fox2);
    }

    @Test
    @SpecAssertion(section = DEPENDENT_DESTRUCTION_EE, id = "eee")
    public void testDependentsDestroyedWhenElEvaluationCompletes() throws Exception {
        // Reset test class
        Fox.reset();
        FoxRun.setDestroyed(false);

        getCurrentConfiguration().getEl().evaluateValueExpression(getCurrentManager(), "#{foxRun}", FoxRun.class);
        assert FoxRun.isDestroyed();
        assert Fox.isDestroyed();
    }
}
