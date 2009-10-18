package org.jboss.jsr299.tck.tests.definition.stereotype;

import java.lang.annotation.Annotation;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091018")
public class StereotypeDefinitionTest extends AbstractJSR299Test
{
   private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {};

   @Test
   @SpecAssertions( { @SpecAssertion(section = "2.7.1.1", id = "aa"), 
	   @SpecAssertion(section = "2.4.3", id = "c") })
   public void testStereotypeWithScopeType()
   {
      assert getBeans(Moose.class).size() == 1;
      assert getBeans(Moose.class).iterator().next().getScope().equals(RequestScoped.class);
   }

   @Test
   @SpecAssertions( { @SpecAssertion(section = "2.7.1.1", id = "aa"), 
	   @SpecAssertion(section = "2.4.4", id = "b") })
   public void testStereotypeWithoutScopeType()
   {
      assert getBeans(Reindeer.class).size() == 1;
      assert getBeans(Reindeer.class).iterator().next().getScope().equals(Dependent.class);
   }
      
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "2.7", id = "c"),
      @SpecAssertion(section = "2.7.1", id = "b")
   })
   public void testOneStereotypeAllowed()
   {
      Bean<LongHairedDog> bean = getBeans(LongHairedDog.class).iterator().next();
      assert bean.getScope().equals(RequestScoped.class);
   }

   @Test
   @SpecAssertions( { @SpecAssertion(section = "2.7.2", id = "e"), 
	   @SpecAssertion(section = "2.7", id = "d") })
   public void testMultipleStereotypesAllowed()
   {
      assert getBeans(HighlandCow.class, TAME_LITERAL).size() == 1;
      Bean<HighlandCow> highlandCow = getBeans(HighlandCow.class, TAME_LITERAL).iterator().next();
      assert highlandCow.getName() == null;
      assert highlandCow.getQualifiers().contains(TAME_LITERAL);
      assert highlandCow.getScope().equals(RequestScoped.class);
   }

   @Test
   @SpecAssertions( { @SpecAssertion(section = "2.7.2", id = "e"), 
	   @SpecAssertion(section = "2.4.4", id = "e") })
   public void testExplicitScopeOverridesMergedScopesFromMultipleStereotype()
   {
      assert getBeans(Springbok.class).size() == 1;
      assert getBeans(Springbok.class).iterator().next().getScope().equals(RequestScoped.class);
   }

   @Test
   @SpecAssertion(section = "4.1", id = "ab")
   public void testStereotypeDeclaredInheritedIsInherited() throws Exception
   {
      assert getBeans(BorderCollie.class).iterator().next().getScope().equals(RequestScoped.class);
   }

   @Test
   @SpecAssertion(section = "4.1", id = "aba")
   public void testStereotypeNotDeclaredInheritedIsNotInherited()
   {
      assert getBeans(ShetlandPony.class).size() == 1;
      assert getBeans(ShetlandPony.class).iterator().next().getScope().equals(Dependent.class);
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "ah")
   public void testStereotypeDeclaredInheritedIsIndirectlyInherited()
   {
      assert getBeans(EnglishBorderCollie.class).iterator().next().getScope().equals(RequestScoped.class);
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "aha")
   public void testStereotypeNotDeclaredInheritedIsNotIndirectlyInherited()
   {
      assert getBeans(MiniatureClydesdale.class).size() == 1;
      assert getBeans(MiniatureClydesdale.class).iterator().next().getScope().equals(Dependent.class);      
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "hhh")
   public void testStereotypeScopeIsOverriddenByInheritedScope()
   {
      assert getBeans(Chihuahua.class).iterator().next().getScope().equals(SessionScoped.class);
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "hhi")
   public void testStereotypeScopeIsOverriddenByIndirectlyInheritedScope()
   {
      assert getBeans(MexicanChihuahua.class).iterator().next().getScope().equals(SessionScoped.class);
   }

}
