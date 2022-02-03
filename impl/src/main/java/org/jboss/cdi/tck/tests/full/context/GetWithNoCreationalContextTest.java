package org.jboss.cdi.tck.tests.full.context;

import static org.jboss.cdi.tck.cdi.Sections.CONTEXT;

import jakarta.enterprise.context.SessionScoped;
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
public class GetWithNoCreationalContextTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(GetWithNoCreationalContextTest.class).build();
    }

    @Test(groups = TestGroups.CDI_FULL)
    @SpecAssertion(section = CONTEXT, id = "na")
    @SpecAssertion(section = CONTEXT, id = "k")
    public void testGetWithoutCreationalContextReturnsNull() {
        Contextual<MySessionBean> mySessionBean = getBeans(MySessionBean.class).iterator().next();
        assert getCurrentManager().getContext(SessionScoped.class).get(mySessionBean) == null;
    }

}
