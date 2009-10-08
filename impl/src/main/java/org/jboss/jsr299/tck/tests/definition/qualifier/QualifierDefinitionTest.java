package org.jboss.jsr299.tck.tests.definition.qualifier;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.inject.New;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.jsr299.tck.literals.DefaultLiteral;
import org.jboss.jsr299.tck.literals.NewLiteral;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class QualifierDefinitionTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "2.3.1", id = "a0"),
      @SpecAssertion(section = "2.3.1", id = "aa")
   })
   public void testDefaultQualifierDeclaredInJava()
   {
      Bean<Order> order = getBeans(Order.class).iterator().next();
      assert order.getQualifiers().size() == 2;
      assert order.getQualifiers().contains(new DefaultLiteral());
      assert order.getQualifiers().contains(new AnyLiteral());
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "2.3.1", id = "b"),
      @SpecAssertion(section = "11.1", id = "c")
   })
   public void testDefaultQualifierForInjectionPoint()
   {
      Bean<Order> order = getBeans(Order.class).iterator().next();
      InjectionPoint injectionPoint = order.getInjectionPoints().iterator().next();
      assert injectionPoint.getBean().getQualifiers().contains(new DefaultLiteral());
   }
   
   @Test
   @SpecAssertion(section = "2.3.1", id = "a0")
   public void testNewQualifierAndAnyBindingMutualExclusive()
   {
      New newOrderProcessor = new NewLiteral()
      {
         
         public Class<?> value()
         {
            return OrderProcessor.class;
         }
      };
      assert getBeans(OrderProcessor.class, newOrderProcessor).size() == 1;
      Bean<OrderProcessor> order = getBeans(OrderProcessor.class, newOrderProcessor).iterator().next();
      assert order.getQualifiers().size() == 1;
      assert order.getQualifiers().iterator().next().equals(newOrderProcessor);
   }

   @Test(groups = { "annotationDefinition", "rewrite" })
   @SpecAssertion(section = "2.3.2", id = "ba")
   public void testQualifierDeclaresBindingAnnotation()
   {
      // Probably can use new SPI for this...
      assert !getBeans(Tarantula.class, new TameQualifier()).isEmpty();
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "2.3.3", id = "a"),
      @SpecAssertion(section="3.1.3", id="be")
   })
   public void testQualifiersDeclaredInJava()
   {
      Bean<Cat> cat = getBeans(Cat.class, new SynchronousQualifier()).iterator().next();
      assert cat.getQualifiers().size() == 2;
      assert cat.getQualifiers().contains(new SynchronousQualifier());
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "2.3.3", id = "d")
   })
   public void testMultipleQualifiers()
   {
      Bean<?> model = getBeans(Cod.class, new ChunkyQualifier(true), new WhitefishQualifier()).iterator().next();
      assert model.getQualifiers().size() == 4;
   }

   @Test(groups = { "injection", "producerMethod" })
   @SpecAssertion(section = "2.3.5", id = "a")
   public void testFieldInjectedFromProducerMethod() throws Exception
   {
	  Bean<Barn> barnBean = getBeans(Barn.class).iterator().next();
      Barn barn = barnBean.create(getCurrentManager().createCreationalContext(barnBean));
      assert barn.petSpider != null;
      assert barn.petSpider instanceof DefangedTarantula;
   }

   @Test
   @SpecAssertion(section = "4.1", id = "aa")
   public void testQualifierDeclaredInheritedIsInherited() throws Exception
   {
      Set<? extends Annotation> bindings = getBeans(BorderCollie.class, new HairyQualifier(false)).iterator().next().getQualifiers();
      assert bindings.size() == 2;
      assert bindings.contains(new HairyQualifier(false));
      assert bindings.contains(new AnyLiteral());
   }

   @Test
   @SpecAssertion(section = "4.1", id = "aaa")
   public void testQualifierNotDeclaredInheritedIsNotInherited()
   {
      Set<? extends Annotation> bindings = getBeans(ShetlandPony.class).iterator().next().getQualifiers();
      assert bindings.size() == 2;
      assert bindings.contains(new DefaultLiteral());
      assert bindings.contains(new AnyLiteral());
   }

   @Test
   @SpecAssertion(section = "4.1", id = "aa")
   public void testQualifierDeclaredInheritedIsBlockedByIntermediateClass()
   {
      Set<? extends Annotation> bindings = getBeans(ClippedBorderCollie.class, new HairyQualifier(true)).iterator().next().getQualifiers();
      assert bindings.size() == 2;
      Annotation hairyLiteral = new HairyQualifier(true);
      assert bindings.contains(hairyLiteral);
      assert bindings.contains(new AnyLiteral());
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "ag")
   public void testQualifierDeclaredInheritedIsIndirectlyInherited()
   {
      Set<? extends Annotation> bindings = getBeans(EnglishBorderCollie.class, new HairyQualifier(false)).iterator().next().getQualifiers();
      assert bindings.size() == 2;
      assert bindings.contains(new HairyQualifier(false));
   }

   @Test
   @SpecAssertion(section = "4.1", id = "aga")
   public void testQualifierNotDeclaredInheritedIsNotIndirectlyInherited()
   {
      Set<? extends Annotation> bindings = getBeans(MiniatureShetlandPony.class).iterator().next().getQualifiers();
      assert bindings.size() == 2;
      assert bindings.contains(new DefaultLiteral());
      assert bindings.contains(new AnyLiteral());      
   }

}
