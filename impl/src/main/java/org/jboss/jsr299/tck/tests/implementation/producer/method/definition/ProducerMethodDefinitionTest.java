package org.jboss.jsr299.tck.tests.implementation.producer.method.definition;

import java.lang.annotation.Annotation;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.IllegalProductException;
import javax.enterprise.inject.TypeLiteral;
import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.jsr299.tck.literals.DefaultLiteral;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="PFD2")
public class ProducerMethodDefinitionTest extends AbstractJSR299Test
{
   
   private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {}; 
   private static final Annotation DEADLIEST_LITERAL = new AnnotationLiteral<Deadliest>() {};

   @Test(groups = "producerMethod")
   @SpecAssertions( { 
      @SpecAssertion(section = "3.3", id = "b"),
      @SpecAssertion(section = "5.6.6", id = "a")
   } )
   public void testStaticMethod() throws Exception
   {
      assert getBeans(String.class).size() == 1;
      assert getInstanceByType(String.class).equals(BeanWithStaticProducerMethod.getString());
   }

   @Test(groups = { "producerMethod" })
   @SpecAssertions( { 
      @SpecAssertion(section = "3.3", id = "aa")
   } )
   public void testProducerOnNonBean() throws Exception
   {
      assert getBeans(Cherry.class).isEmpty();
   }

   @Test(groups = "producerMethod")
   @SpecAssertions( { 
      @SpecAssertion(section = "3.3.5", id = "b")
   } )
   public void testStaticDisposerMethod() throws Exception
   {
      assert getBeans(String.class).size() == 1;
      String aString = getInstanceByType(String.class);
      Bean<String> stringBean = getBeans(String.class).iterator().next();
      CreationalContext<String> creationalContext = getCurrentManager().createCreationalContext(stringBean);
      stringBean.destroy(aString, creationalContext);
      assert BeanWithStaticProducerMethod.stringDestroyed;
   }

   @Test(groups = "producerMethod")
   @SpecAssertion(section = "3.3", id = "ga")
   public void testParameterizedReturnType() throws Exception
   {
      assert getBeans(new TypeLiteral<FunnelWeaver<Spider>>() {}).size() == 1;
   }

   @Test(groups = "producerMethod")
   @SpecAssertions({
      @SpecAssertion(section = "3.3", id = "c"),
      @SpecAssertion(section = "3.3.2", id = "a"),
      @SpecAssertion(section = "2.3.1", id = "a0"),
      @SpecAssertion(section = "2.3.1", id = "aa")
   })
   public void testDefaultBindingType() throws Exception
   {
      assert getCurrentManager().getBeans(Tarantula.class).size() == 1;
      assert getCurrentManager().getBeans(Tarantula.class).iterator().next().getQualifiers().size() == 2;
      assert getCurrentManager().getBeans(Tarantula.class).iterator().next().getQualifiers().contains(new DefaultLiteral());
      assert getCurrentManager().getBeans(Tarantula.class).iterator().next().getQualifiers().contains(new AnyLiteral());
   }

   @Test(groups = "producerMethod")
   @SpecAssertions({
      @SpecAssertion(section = "3.3.1", id = "c"),
      @SpecAssertion(section="2.2", id = "l")
   })
   public void testApiTypeForClassReturn() throws Exception
   {
      assert getBeans(Tarantula.class).size() == 1;
      Bean<Tarantula> tarantula = getBeans(Tarantula.class).iterator().next();
      
      assert tarantula.getTypes().size() == 6;
      assert tarantula.getTypes().contains(Tarantula.class);
      assert tarantula.getTypes().contains(DeadlySpider.class);
      assert tarantula.getTypes().contains(Spider.class);
      assert tarantula.getTypes().contains(Animal.class);
      assert tarantula.getTypes().contains(DeadlyAnimal.class);
      assert tarantula.getTypes().contains(Object.class);
   }

   @Test(groups = "producerMethod")
   @SpecAssertion(section = "3.3.1", id = "a")
   public void testApiTypeForInterfaceReturn() throws Exception
   {
      assert getBeans(Bite.class).size() == 1;
      Bean<Bite> animal = getBeans(Bite.class).iterator().next();
      assert animal.getTypes().size() == 2;
      assert animal.getTypes().contains(Bite.class);
      assert animal.getTypes().contains(Object.class);
   }

   @Test(groups = "producerMethod")
   @SpecAssertion(section = "3.3.1", id = "ba")
   public void testApiTypeForPrimitiveReturn() throws Exception
   {
      assert getBeans(Integer.class).size() == 1;
      Bean<Integer> integer = getBeans(Integer.class).iterator().next();
      assert integer.getTypes().size() == 2;
      assert integer.getTypes().contains(int.class);
      assert integer.getTypes().contains(Object.class);
   }

   @Test(groups = "producerMethod")
   @SpecAssertions({
      @SpecAssertion(section = "3.3.1", id = "bb"), 
      @SpecAssertion(section = "2.2.1", id = "i") 
   })
   public void testApiTypeForArrayTypeReturn() throws Exception
   {
      assert getBeans(Spider[].class).size() == 1;
      Bean<Spider[]> spiders = getBeans(Spider[].class).iterator().next();
      assert spiders.getTypes().size() == 2;
      assert spiders.getTypes().contains(Spider[].class);
      assert spiders.getTypes().contains(Object.class);
   }

   @Test(groups = "producerMethod")
   @SpecAssertions({
      @SpecAssertion(section = "3.3.2", id = "be"),
      @SpecAssertion(section = "3.3", id = "k"),
      @SpecAssertion(section="2.3.3", id="b")
   })
   public void testBindingType() throws Exception
   {
      assert getBeans(Tarantula.class, TAME_LITERAL).size() == 1;
      Bean<Tarantula> tarantula = getBeans(Tarantula.class, TAME_LITERAL).iterator().next();
      assert tarantula.getQualifiers().size() == 2;
      assert tarantula.getQualifiers().contains(TAME_LITERAL);
   }

   @Test(groups = "producerMethod")
   @SpecAssertions({
      @SpecAssertion(section = "3.3.2", id = "ba"),
      @SpecAssertion(section = "3.3", id = "k")
   })
   public void testScopeType() throws Exception
   {
      assert getBeans(DaddyLongLegs.class, TAME_LITERAL).size() == 1;
      Bean<DaddyLongLegs> daddyLongLegs = getBeans(DaddyLongLegs.class, TAME_LITERAL).iterator().next();
      assert daddyLongLegs.getScope().equals(RequestScoped.class);
   }

   @Test(groups = "producerMethod")
   @SpecAssertions({
      @SpecAssertion(section = "3.3.2", id = "bb"),
      @SpecAssertion(section = "2.5.1", id = "b")
   })
   public void testNamedMethod() throws Exception
   {
      assert getBeans(BlackWidow.class, TAME_LITERAL).size() == 1;
      Bean<BlackWidow> blackWidowSpider = getBeans(BlackWidow.class, TAME_LITERAL).iterator().next();
      assert blackWidowSpider.getName().equals("blackWidow");
   }

   @Test(groups = "producerMethod")
   @SpecAssertions({
      @SpecAssertion(section = "3.3.2", id = "bb"),
      @SpecAssertion(section = "2.5.2", id = "b"),
      @SpecAssertion(section = "2.5.1", id = "d"),
      @SpecAssertion(section = "3.3.10", id = "a")
   })
   public void testDefaultNamedMethod() throws Exception
   {
      assert getBeans(DaddyLongLegs.class, TAME_LITERAL).size() == 1;
      Bean<DaddyLongLegs> daddyLongLegsSpider = getBeans(DaddyLongLegs.class, TAME_LITERAL).iterator().next();
      assert daddyLongLegsSpider.getName().equals("produceDaddyLongLegs");
   }




   // Review 2.2
   @Test(groups = "producerMethod")
   @SpecAssertions({ 
      @SpecAssertion(section = "2.7.2", id = "b"), 
      @SpecAssertion(section = "3.3.2", id = "ba"), 
      @SpecAssertion(section = "2.4.4", id = "c"),
      @SpecAssertion(section = "3.3.2", id = "bd")
   })
   public void testStereotypeSpecifiesScope() throws Exception
   {
      assert getBeans(WolfSpider.class, TAME_LITERAL).size() == 1;
      Bean<WolfSpider> wolfSpider = getBeans(WolfSpider.class, TAME_LITERAL).iterator().next();
      assert wolfSpider.getScope().equals(RequestScoped.class);
   }

   @Test
   @SpecAssertion(section = "4.2", id = "da")
   public void testNonStaticProducerMethodInheritedBySpecializingSubclass()
   {
      assert getBeans(Egg.class, new AnnotationLiteral<Yummy>() {}).size() == 1;
      assert getInstanceByType(Egg.class,new AnnotationLiteral<Yummy>() {}).getMother().getClass().equals(AndalusianChicken.class);
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "4.2", id = "da"),
      @SpecAssertion(section = "4.2", id = "dg")
   })
   public void testNonStaticProducerMethodNotInherited()
   {
      assert getBeans(Apple.class, new AnnotationLiteral<Yummy>() {}).size() == 1;
      assert getInstanceByType(Apple.class,new AnnotationLiteral<Yummy>() {}).getTree().getClass().equals(AppleTree.class);      
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "2.3.5", id = "a"),
      @SpecAssertion(section = "3.3.3", id = "c"),
      @SpecAssertion(section = "3.3.3", id = "b")
   })
   public void testBindingTypesAppliedToProducerMethodParameters()
   {
      Bean<Tarantula> tarantula = getBeans(Tarantula.class, DEADLIEST_LITERAL).iterator().next();
      CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantula);
      Tarantula instance = tarantula.create(creationalContext);
      assert instance.getDeathsCaused() == 1;
   }
   
   @Test
   @SpecAssertion(section = "3.3", id = "e")
   public void testDependentProducerReturnsNullValue()
   {
      assert getInstanceByType(Acorn.class) == null;
   }
   
   @Test(expectedExceptions = { IllegalProductException.class })
   @SpecAssertion(section = "3.3", id = "f")
   public void testNonDependentProducerReturnsNullValue()
   {
      getInstanceByType(Pollen.class, new AnnotationLiteral<Yummy>() {}).ping();
      assert false;
   }
}
