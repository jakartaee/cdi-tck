package org.jboss.jsr299.tck.tests.context;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Contextual;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class GetWithNoCreationalContextTest extends AbstractJSR299Test
{
   @Test(groups = { "contexts" })
   @SpecAssertions({
      @SpecAssertion(section = "6.2", id = "na"),
      @SpecAssertion(section = "6.2", id = "k")
   })
   public void testGetWithoutCreationalContextReturnsNull()
   {
      Contextual<MySessionBean> mySessionBean = getBeans(MySessionBean.class).iterator().next();
      assert getCurrentManager().getContext(SessionScoped.class).get(mySessionBean) == null;
   }
   
}
