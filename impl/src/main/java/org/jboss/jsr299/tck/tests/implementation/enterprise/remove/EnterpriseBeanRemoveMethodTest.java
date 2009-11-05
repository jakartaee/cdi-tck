package org.jboss.jsr299.tck.tests.implementation.enterprise.remove;

import static org.jboss.testharness.impl.packaging.PackagingType.EAR;

import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.testng.annotations.Test;

/**
 *
 * @author Nicklas Karlsson
 */
@Artifact
@Packaging(EAR)
@IntegrationTest
@SpecVersion(spec="cdi", version="20091101")
public class EnterpriseBeanRemoveMethodTest extends AbstractJSR299Test
{
   
   @Test(groups = { "enterpriseBeans", "removeMethod", "lifecycle" })
   @SpecAssertion(section = "3.2.1", id = "a")
   public void testApplicationMayCallAnyRemoveMethodOnDependentScopedSessionEnterpriseBeans() throws Exception
   {
      Bean<?> bean = getCurrentManager().getBeans(StateKeeper.class).iterator().next();
      StateKeeper stateKeeper = (StateKeeper)
      getCurrentManager().getReference(bean,StateKeeper.class, getCurrentManager().createCreationalContext(bean));
      stateKeeper.setRemoveCalled(false);      
      
      DependentSessionInterface sessionBean = getInstanceByType(DependentSessionInterface.class);
      sessionBean.remove();
      assert stateKeeper.isRemoveCalled();
   }

   @Test(groups = { "enterpriseBeans", "removeMethod", "lifecycle" })
   @SpecAssertion(section = "3.2.1", id = "da")
   public void testApplicationMayCallRemoveMethodOnDependentScopedSessionEnterpriseBeansButNoParametersArePassed() throws Exception
   {
      DependentSessionInterface sessionBean = getInstanceByType(DependentSessionInterface.class);
      sessionBean.anotherRemoveWithParameters("required", null);
      StateKeeper stateKeeper = getInstanceByType(StateKeeper.class);
      assert stateKeeper.isRemoveCalled();
   }

   @Test(groups = { "enterpriseBeans", "removeMethod", "lifecycle" }, expectedExceptions = UnsupportedOperationException.class)
   @SpecAssertion(section = "3.2.1", id = "b")
   public void testApplicationCannotCallRemoveMethodOnNonDependentScopedSessionEnterpriseBean()
   {
      SessionScopedSessionInterface sessionBean = getInstanceByType(SessionScopedSessionInterface.class);
      sessionBean.remove();
      assert false : "Should never reach this assertion";
   }

}
