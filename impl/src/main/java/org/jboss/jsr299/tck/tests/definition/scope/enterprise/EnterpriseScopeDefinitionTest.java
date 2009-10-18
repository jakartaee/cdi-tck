package org.jboss.jsr299.tck.tests.definition.scope.enterprise;

import javax.enterprise.context.RequestScoped;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

@Artifact
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="20091018")
public class EnterpriseScopeDefinitionTest extends AbstractJSR299Test
{
   @Test 
   @SpecAssertion(section="4.1", id = "be")
   public void testScopeTypeDeclaredInheritedIsInherited() throws Exception
   {
      assert getBeans(BorderCollieLocal.class).iterator().next().getScope().equals(RequestScoped.class);
   }
   
   @Test @SpecAssertion(section="4.1", id = "bea")
   public void testScopeTypeNotDeclaredInheritedIsNotInherited() throws Exception
   {
      assert !getBeans(SiameseLocal.class).iterator().next().getScope().equals(FooScoped.class);
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "bh")
   public void testScopeTypeDeclaredInheritedIsIndirectlyInherited()
   {
      assert getBeans(EnglishBorderCollieLocal.class).iterator().next().getScope().equals(RequestScoped.class);
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "bha")
   public void testScopeTypeNotDeclaredInheritedIsNotIndirectlyInherited()
   {
      assert !getBeans(BengalTigerLocal.class).iterator().next().getScope().equals(FooScoped.class);
   }
}
