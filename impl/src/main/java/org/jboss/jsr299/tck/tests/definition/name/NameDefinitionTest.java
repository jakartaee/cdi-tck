package org.jboss.jsr299.tck.tests.definition.name;

import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class NameDefinitionTest extends AbstractJSR299Test
{
   
   @Test 
   @SpecAssertions({
      @SpecAssertion(section="2.5.1", id = "a"),
      @SpecAssertion(section="2", id="e"),
      @SpecAssertion(section = "3.1.3", id = "bb")
   })
   public void testNonDefaultNamed()
   {
      assert getBeans(Moose.class).size() == 1;
      Bean<Moose> moose =getBeans(Moose.class).iterator().next(); 
      assert moose.getName().equals("aMoose");
   }
   
   @Test 
   @SpecAssertions({
      @SpecAssertion(section= "2.5.2", id = "a"),
      @SpecAssertion(section = "3.1.5", id = "a"),
      @SpecAssertion(section = "2.5.1", id = "d")
   })
   public void testDefaultNamed()
   {
      assert getBeans(Haddock.class).size() == 1; 
      Bean<Haddock> haddock = getBeans(Haddock.class).iterator().next();
      assert haddock.getName() != null;
      assert haddock.getName().equals("haddock");
   }
   
   @Test 
   @SpecAssertions({ 
      @SpecAssertion(section = "2.7", id = "a"),
      @SpecAssertion(section = "2.7.1.3", id = "aaa")
   })
   public void testStereotypeDefaultsName()
   {
      assert getBeans(RedSnapper.class).size() == 1; 
      Bean<RedSnapper> bean = getBeans(RedSnapper.class).iterator().next();
      assert bean.getName().equals("redSnapper");
   }
   
   @Test 
   @SpecAssertions({
      @SpecAssertion(section="2.5.3", id = "a"),
      @SpecAssertion(section="2", id="e")
   })
   public void testNotNamedInJava()
   {
      assert getBeans(SeaBass.class).size() == 1; 
      Bean<SeaBass> bean = getBeans(SeaBass.class).iterator().next();
      assert bean.getName() == null;
   }
   
   @Test @SpecAssertion(section="2.5.3", id = "a")
   public void testNotNamedInStereotype()
   {
      assert getBeans(Minnow.class).size() == 1; 
      Bean<Minnow> bean = getBeans(Minnow.class).iterator().next();
      assert bean.getName() == null;
   }
   
}
