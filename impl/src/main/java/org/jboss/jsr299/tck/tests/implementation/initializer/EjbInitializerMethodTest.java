package org.jboss.jsr299.tck.tests.implementation.initializer;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

@Artifact
@Packaging(PackagingType.EAR)
@IntegrationTest
@SpecVersion(spec="cdi", version="20091101")
public class EjbInitializerMethodTest extends AbstractJSR299Test
{
   @Test(groups = { "initializerMethod", "ejb3" })
   @SpecAssertions({
      @SpecAssertion(section = "3.9", id = "e")
   })
   // This DOES NOT TEST initializer methods on Java EE component classes PLM
   public void testInitializerMethodNotABusinessMethod()
   {
      AndalusianChicken.nonBusinessMethodCalled = false;
      getInstanceByType(LocalChicken.class).cluck();
      assert AndalusianChicken.nonBusinessMethodCalled = true;
   }
}
