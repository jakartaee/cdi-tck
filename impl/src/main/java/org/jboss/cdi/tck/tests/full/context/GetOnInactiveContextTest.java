package org.jboss.cdi.tck.tests.full.context;

import static org.jboss.cdi.tck.cdi.Sections.CONTEXT;

import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.context.spi.Contextual;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.TestGroups;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class GetOnInactiveContextTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(GetFromContextualTest.class).build();
    }

    @Test(expectedExceptions = { ContextNotActiveException.class }, groups = TestGroups.CDI_FULL)
    @SpecAssertion(section = CONTEXT, id = "m")
    public void testInvokingGetOnInactiveContextFails() {
        Context sessionContext = getCurrentManager().getContext(SessionScoped.class);
        assert sessionContext.isActive();
        setContextInactive(sessionContext);

        Contextual<MySessionBean> mySessionBean = getBeans(MySessionBean.class).iterator().next();
        sessionContext.get(mySessionBean);
    }

}
