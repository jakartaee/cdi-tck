package org.jboss.cdi.tck.tests.full.context;

import static org.jboss.cdi.tck.cdi.Sections.CONTEXT;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.inject.spi.Bean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.TestGroups;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.spi.CreationalContexts;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Nicklas Karlsson
 * @author Pete Muir
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class DestroyForSameCreationalContextTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DestroyForSameCreationalContextTest.class).build();
    }

    @Test(groups = TestGroups.CDI_FULL)
    @SpecAssertion(section = CONTEXT, id = "r")
    public void testDestroyForSameCreationalContextOnly() {
        // Check that the cc is called (via cc.release()) when we request a context destroyed
        // Note that this is an indirect effect
        Context sessionContext = getCurrentManager().getContext(SessionScoped.class);

        Bean<AnotherSessionBean> sessionBean = getBeans(AnotherSessionBean.class).iterator().next();

        CreationalContexts.Inspectable<AnotherSessionBean> creationalContext = createInspectableCreationalContext(sessionBean);
        AnotherSessionBean instance = sessionContext.get(sessionBean, creationalContext);
        instance.ping();

        destroyContext(sessionContext);
        assert creationalContext.isReleaseCalled();

    }

}
