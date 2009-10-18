package org.jboss.jsr299.tck.tests.inheritance.specialization.simple;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.inject.Named;

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
public class SimpleBeanSpecializationTest extends AbstractJSR299Test
{

   private static Annotation LANDOWNER_LITERAL = new AnnotationLiteral<Landowner>()
   {
   };

   @SuppressWarnings("unchecked")
   @Test
   @SpecAssertions( { 
      @SpecAssertion(section = "4.3.1", id = "ia"),
      @SpecAssertion(section = "4.3.1", id = "ib"),
      @SpecAssertion(section = "4.3.1", id = "j"), 
      @SpecAssertion(section = "3.1.4", id = "aa") 
   })
   public void testSpecializingBeanHasQualifiersOfSpecializedAndSpecializingBean()
   {
      assert getBeans(LazyFarmer.class, LANDOWNER_LITERAL).size() == 1;
      Bean<?> bean = getBeans(LazyFarmer.class, LANDOWNER_LITERAL).iterator().next();
      assert bean.getTypes().contains(Farmer.class);
      assert bean.getQualifiers().size() == 4;
      assert annotationSetMatches(bean.getQualifiers(), Landowner.class, Lazy.class, Any.class, Named.class);
   }

   @Test
   @SpecAssertions( { 
      @SpecAssertion(section = "4.3.1", id = "k"), 
      @SpecAssertion(section = "3.1.4", id = "ab") 
   })
   public void testSpecializingBeanHasNameOfSpecializedBean()
   {
      assert getBeans(LazyFarmer.class, LANDOWNER_LITERAL).size() == 1;
      assert "farmer".equals(getBeans(LazyFarmer.class, LANDOWNER_LITERAL).iterator().next().getName());
   }

   @Test
   @SpecAssertions( { 
     // @SpecAssertion(section = "4.3.2", id = "ab"), removed from spec
      @SpecAssertion(section="4.3", id="cb")
   })
   public void testProducerMethodOnSpecializedBeanCalledOnSpecializingBean()
   {
      assert getBeans(Waste.class).size() == 1;
      assert getInstanceByType(Waste.class).getFrom().equals(Office.class.getName());
   }
}
