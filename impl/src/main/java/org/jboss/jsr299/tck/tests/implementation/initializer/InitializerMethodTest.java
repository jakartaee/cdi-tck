package org.jboss.jsr299.tck.tests.implementation.initializer;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

@Artifact
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="20091018")
public class InitializerMethodTest extends AbstractJSR299Test
{

   @Test(groups = { "initializerMethod" })
   @SpecAssertions({
      @SpecAssertion(section = "3.9.2", id = "b"),
      @SpecAssertion(section = "2.3.5", id = "b")
   })
   public void testBindingTypeOnInitializerParameter()
   {
      PremiumChickenHutch hutch = getInstanceByType(PremiumChickenHutch.class);
      assert hutch.getChicken().getName().equals("Preferred");
      StandardChickenHutch anotherHutch = getInstanceByType(StandardChickenHutch.class);
      assert anotherHutch.getChicken().getName().equals("Standard");
   }

   @Test(groups = { "initializerMethod" })
   @SpecAssertions({ 
      @SpecAssertion(section = "3.9", id = "g"), 
      @SpecAssertion(section = "3.9.1", id = "a"),
      @SpecAssertion(section = "3.9.2", id = "aa"),
      @SpecAssertion(section = "5.6.4", id = "ad"),
      @SpecAssertion(section = "3.10", id = "a")
   })
   public void testMultipleInitializerMethodsAreCalled()
   {
      ChickenHutch chickenHutch = getInstanceByType(ChickenHutch.class);
      assert chickenHutch.fox != null;
      assert chickenHutch.chicken != null;
   }

}