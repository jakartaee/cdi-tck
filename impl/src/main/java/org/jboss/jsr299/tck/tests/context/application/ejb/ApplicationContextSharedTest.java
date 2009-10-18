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
@SpecVersion(spec="cdi", version="20091018")
public class ApplicationContextSharedTest extends AbstractJSR299Test
{

   @Test(groups = { "contexts", "ejb3", "integration", "rewrite" })
   // TODO Needs to test *all* uses of app context are shared, not each type individually
   @SpecAssertion(section = "6.7.3", id = "e")
   public void testApplicationContextShared() throws Exception
   {
      FMS flightManagementSystem = getInstanceByType(FMS.class);
      flightManagementSystem.climb();
      flightManagementSystem.descend();
      Thread.sleep(250);
      assert flightManagementSystem.isSameBean();
   }

}
