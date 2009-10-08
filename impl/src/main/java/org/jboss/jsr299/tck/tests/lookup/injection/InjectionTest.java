package org.jboss.jsr299.tck.tests.lookup.injection;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="PFD2")
public class InjectionTest extends AbstractJSR299Test
{
   @Test(groups = { "injection", "producerMethod" })
   @SpecAssertion(section = "5.3.4", id = "aa")
   public void testInjectionPerformsBoxingIfNecessary() throws Exception
   {
      assert getBeans(SpiderNest.class).size() == 1;
      SpiderNest spiderNest = getInstanceByType(SpiderNest.class);
      assert spiderNest.numberOfSpiders != null;
      assert spiderNest.numberOfSpiders.equals(4);
   }
   
   @Test
   @SpecAssertion(section = "5.3", id = "kc")
   public void testInjectionOfNamedBean()
   {
      WolfPack wolfPack = getInstanceByType(WolfPack.class);
      assert wolfPack.getAlphaWolf() != null;
   }

   @Test 
   @SpecAssertion(section="4.2", id = "aa")
   public void testFieldDeclaredInSuperclassInjected() throws Exception
   {      
     DeluxeHenHouse henHouse = getInstanceByType(DeluxeHenHouse.class);
     assert henHouse.fox != null;
     assert henHouse.fox.getName().equals("gavin");
   }
   
   @Test 
   @SpecAssertion(section="4.2", id = "ac")
   public void testFieldDeclaredInIndirectSuperclassInjected() throws Exception
   {
      MegaPoorHenHouse henHouse = getInstanceByType(MegaPoorHenHouse.class);
      assert henHouse.fox != null;
      assert henHouse.fox.getName().equals("gavin");
   }

}
