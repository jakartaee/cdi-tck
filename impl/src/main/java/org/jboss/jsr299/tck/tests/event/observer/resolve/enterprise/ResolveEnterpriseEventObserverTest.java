package org.jboss.jsr299.tck.tests.event.observer.resolve.enterprise;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class ResolveEnterpriseEventObserverTest extends AbstractJSR299Test
{
   @Test(groups = { "events", "ejb" })
   @SpecAssertion(section = "10.4", id = "d")
   public void testObserverMethodOnEnterpriseBeanIsBusinessMethodOrStatic()
   {
      assert getCurrentManager().resolveObserverMethods(new EJBEvent()).size() == 2;
   }
}
