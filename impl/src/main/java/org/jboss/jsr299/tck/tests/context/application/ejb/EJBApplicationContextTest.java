package org.jboss.jsr299.tck.tests.context.application.ejb;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

/**
 * EJB and related tests with the built-in application context.
 * 
 * @author David Allen
 */
@Artifact
@IntegrationTest
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="20091101")
public class EJBApplicationContextTest extends AbstractJSR299Test
{
   @Test(groups = { "contexts", "ejb3", "integration" })
   @SpecAssertion(section = "6.7.3", id = "dc")
   public void testApplicationScopeActiveDuringCallToEjbTimeoutMethod() throws Exception
   {
      FMS flightManagementSystem = getInstanceByType(FMS.class);
      flightManagementSystem.climb();
      Thread.sleep(250);
      assert flightManagementSystem.isApplicationScopeActive();
   }

}
