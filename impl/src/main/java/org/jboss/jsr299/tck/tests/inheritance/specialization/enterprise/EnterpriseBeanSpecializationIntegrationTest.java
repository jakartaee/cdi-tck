package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@IntegrationTest
@Packaging(PackagingType.EAR)
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091018")
public class EnterpriseBeanSpecializationIntegrationTest extends AbstractJSR299Test
{
   
   private static Annotation LANDOWNER_LITERAL = new AnnotationLiteral<Landowner>() {};
   
   @Test
   @SpecAssertions({
     @SpecAssertion(section = "4.3", id = "ca")
   })
   public void testSpecializedBeanNotInstantiated() throws Exception
   {
      Bean<?> farmerBean = getCurrentManager().resolve(getCurrentManager().getBeans(FarmerLocal.class,LANDOWNER_LITERAL));
      FarmerLocal farmer = (FarmerLocal) getCurrentManager().getReference(farmerBean, Object.class, getCurrentManager().createCreationalContext(farmerBean));
      assert farmer.getClassName().equals(LazyFarmer.class.getName());
   }
   
}
