package org.jboss.jsr299.tck.tests.context;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091018")
public class GetOnInactiveContextTest extends AbstractJSR299Test
{
   @Test(groups = { "contexts" }, expectedExceptions = { ContextNotActiveException.class })
   @SpecAssertion(section = "6.2", id = "m")
   public void testInvokingGetOnInactiveContextFails()
   {
      Context sessionContext = getCurrentManager().getContext(SessionScoped.class);
      assert sessionContext.isActive();
      setContextInactive(sessionContext);

      Contextual<MySessionBean> mySessionBean = getBeans(MySessionBean.class).iterator().next();
      sessionContext.get(mySessionBean);
   }
   
}
