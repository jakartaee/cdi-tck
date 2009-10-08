package org.jboss.jsr299.tck.tests.definition.stereotype.enterprise;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

@IntegrationTest
@Artifact
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="PFD2")
public class EnterpriseStereotypeDefinitionTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "4.1", id = "am")
   public void testStereotypeDeclaredInheritedIsInherited() throws Exception
   {
      assert getBeans(BorderCollieLocal.class).iterator().next().getScope().equals(RequestScoped.class);
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "ama")
   public void testStereotypeNotDeclaredInheritedIsNotInherited() throws Exception
   {
      assert !getBeans(BarracudaLocal.class).iterator().next().getScope().equals(RequestScoped.class);
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "aq")
   public void testStereotypeDeclaredInheritedIsIndirectlyInherited()
   {
      assert getBeans(EnglishBorderCollieLocal.class).iterator().next().getScope().equals(RequestScoped.class);
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "aqa")
   public void testStereotypeNotDeclaredInheritedIsNotIndirectlyInherited()
   {
      assert !getBeans(TameBarracudaLocal.class).iterator().next().getScope().equals(RequestScoped.class);
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "hhj")
   public void testStereotypeScopeIsOverriddenByInheritedScope()
   {
      assert getBeans(ChihuahuaLocal.class).iterator().next().getScope().equals(SessionScoped.class);
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "hhk")
   public void testStereotypeScopeIsOverriddenByIndirectlyInheritedScope()
   {
      assert getBeans(MexicanChihuahuaLocal.class).iterator().next().getScope().equals(SessionScoped.class);
   }
}
