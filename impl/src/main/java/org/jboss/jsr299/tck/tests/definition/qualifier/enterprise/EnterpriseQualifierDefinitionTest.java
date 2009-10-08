package org.jboss.jsr299.tck.tests.definition.qualifier.enterprise;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

/**
 * TODO This test specifically tests session beans even though the assertions are true
 * for all managed beans. So likely there should be a parallel set of tests for session
 * and other managed beans.
 * 
 */
@Artifact
@IntegrationTest
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="PFD2")
public class EnterpriseQualifierDefinitionTest extends AbstractJSR299Test
{
   @SuppressWarnings("unchecked")
   @Test
   @SpecAssertion(section = "4.1", id = "al")
   public void testQualifierDeclaredInheritedIsInherited() throws Exception
   {
      Set<? extends Annotation> qualifiers = getBeans(BorderCollieLocal.class, new HairyQualifier(false)).iterator().next().getQualifiers();
      assert annotationSetMatches(qualifiers, Any.class, Hairy.class);
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "ala")
   public void testQualifierNotDeclaredInheritedIsNotInherited() throws Exception
   {      
      assert getBeans(TameSkinnyHairlessCatLocal.class, new SkinnyQualifier()).size() == 0;
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "ap")
   public void testQualifierDeclaredInheritedIsIndirectlyInherited()
   {
      Set<? extends Annotation> qualifiers = getBeans(EnglishBorderCollieLocal.class, new HairyQualifier(false)).iterator().next().getQualifiers();
      assert annotationSetMatches(qualifiers, Any.class, Hairy.class);
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "apa")
   public void testQualifierNotDeclaredInheritedIsNotIndirectlyInherited()
   {          
      Set<Bean<FamousCatLocal>> beans = getBeans(FamousCatLocal.class, new SkinnyQualifier());
      assert beans.size() == 0;
   }
}
