package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.spi.Bean;

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
@SpecVersion(spec="cdi", version="PFD2")
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
   
   
   //@Test
   //@SpecAssertion(section="4.3.2", id = "aa") removed from spec
   public void testProducerMethodOnSpecializedBeanCalledOnSpecializingBean() throws Exception
   {
      assert getBeans(Waste.class).size() == 1;
      assert getInstanceByType(Waste.class).getFrom().equals(Office.class.getName());
   }
}
