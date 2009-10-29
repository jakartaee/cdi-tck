package org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.Any;
import javax.enterprise.util.AnnotationLiteral;
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
public class ProducerMethodSpecializationTest extends AbstractJSR299Test
{
   
   private static Annotation EXPENSIVE_LITERAL = new AnnotationLiteral<Expensive>() {};
   private static Annotation SPARKLY_LITERAL = new AnnotationLiteral<Sparkly>() {};

   @Test
   @SpecAssertions({
     @SpecAssertion(section = "5.6.6", id = "c"),
     @SpecAssertion(section = "3.3.4", id = "aa")
   })
   public void testSpecializingBeanHasBindingsOfSpecializedAndSpecializingBean()
   {
      assert getCurrentManager().getBeans(Necklace.class, EXPENSIVE_LITERAL, SPARKLY_LITERAL).size() == 1;
      assert getCurrentManager().getBeans(Necklace.class, EXPENSIVE_LITERAL, SPARKLY_LITERAL).iterator().next().getQualifiers().size() == 4;
      assert annotationSetMatches( getCurrentManager().getBeans(Necklace.class, EXPENSIVE_LITERAL, SPARKLY_LITERAL).iterator().next().getQualifiers(), Expensive.class, Sparkly.class, Any.class, Named.class);
      assert getBeans(Necklace.class, new AnnotationLiteral<Sparkly>(){}).size() == 1;
      assert getBeans(Necklace.class, new AnnotationLiteral<Sparkly>(){}).iterator().next().getName().equals("expensiveGift");
      Product product = getInstanceByType(Product.class,EXPENSIVE_LITERAL, SPARKLY_LITERAL);
      assert product instanceof Necklace;
   }
   
}
