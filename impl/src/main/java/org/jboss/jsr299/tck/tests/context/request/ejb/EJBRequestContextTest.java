package org.jboss.jsr299.tck.tests.context.request.ejb;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

/**
 * EJB and related tests with the built-in request context.
 * 
 * @author David Allen
 */
@Artifact
@IntegrationTest
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="20091101")
public class EJBRequestContextTest extends AbstractJSR299Test
{
   /**
    * The request scope is active during any remote method invocation of any EJB
    * bean, during any call to an EJB timeout method and during message delivery
    * to any EJB message driven bean.
    */
   // WBRI-301
   @Test(groups = { "ri-broken", "contexts", "ejb3.1", "integration" })
   @SpecAssertion(section = "6.7.1", id = "gc")
   public void testRequestScopeActiveDuringCallToEjbTimeoutMethod() throws Exception
   {
      FMS flightManagementSystem = getInstanceByType(FMS.class);
      flightManagementSystem.climb();
      Thread.sleep(250);
      assert flightManagementSystem.isRequestScopeActive();
   }

   /**
    * The request context is destroyed after the remote method invocation,
    * timeout or message delivery completes.
    */
   // WBRI-301
   @Test(groups = { "ri-broken", "contexts", "ejb3.1", "integration" })
   @SpecAssertion(section = "6.7.1", id = "hc")
   public void testRequestScopeDestroyedAfterCallToEjbTimeoutMethod() throws Exception
   {
      FMS flightManagementSystem = getInstanceByType(FMS.class);
      flightManagementSystem.climb();
      flightManagementSystem.descend();
      Thread.sleep(250);
      assert !flightManagementSystem.isSameBean();
      assert SimpleRequestBean.isBeanDestroyed();
   }

}
