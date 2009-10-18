package org.jboss.jsr299.tck.tests.definition.bean;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * This test class should be used for common assertions about managed beans (not session beans)
 * 
 * @author Pete Muir
 */
@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class BeanDefinitionTest extends AbstractJSR299Test
{
   
   private static Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {};
   
   // TODO This should actually somehow test the reverse as well - that the container
   // throws a definition exception if any of these occur
   
   @Test
   @SpecAssertion(section = "2", id = "a")
   public void testBeanTypesNonEmpty()
   {
      assert getBeans(RedSnapper.class).size() == 1;
      assert getBeans(RedSnapper.class).iterator().next().getTypes().size() > 0;
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "2", id = "b"),
      @SpecAssertion(section = "11.1", id = "ba")
   })
   public void testQualifiersNonEmpty()
   {
      assert getBeans(RedSnapper.class).size() == 1;
      assert getBeans(RedSnapper.class).iterator().next().getQualifiers().size() > 0;
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "2", id = "c"),
      @SpecAssertion(section = "2.4", id = "a"),
      @SpecAssertion(section = "3.1.3", id = "ba"),
      @SpecAssertion(section = "11.1", id = "ba")
   })
   public void testHasScopeType()
   {
      assert getBeans(RedSnapper.class).size() == 1;
      assert getBeans(RedSnapper.class).iterator().next().getScope().equals(RequestScoped.class);
   }
   
   @Test(groups = "producerMethod")
   @SpecAssertions({
      @SpecAssertion(section = "2.2.1", id = "j"),
      @SpecAssertion(section = "5.3.4", id = "ba"),
      @SpecAssertion(section = "11.1", id = "bd")
   })
   public void testIsNullable() throws Exception
   {
      assert getBeans(int.class).size() == 1;
      Bean<Integer> bean = getBeans(int.class).iterator().next();
      assert !bean.isNullable();
      assert getBeans(Animal.class, TAME_LITERAL).size() == 1;
      Bean<Animal> animalBean = getBeans(Animal.class, TAME_LITERAL).iterator().next();
      assert animalBean.isNullable();
   }
   
   @Test
   @SpecAssertions({
     @SpecAssertion(section = "3.1.2", id = "a"),
     @SpecAssertion(section = "2.2", id = "a"),
     @SpecAssertion(section = "2.2.1", id = "a"),
     @SpecAssertion(section = "2.2.1", id = "d"),
     @SpecAssertion(section = "2.2.1", id = "e"),
     @SpecAssertion(section = "2.2", id = "l"),
     @SpecAssertion(section = "11.1", id = "ba")
   })
   public void testBeanTypes()
   {
      assert getBeans(Tarantula.class).size() == 1;
      Bean<Tarantula> bean = getBeans(Tarantula.class).iterator().next();
      assert bean.getTypes().size() == 6;
      assert bean.getTypes().contains(Tarantula.class);
      assert bean.getTypes().contains(Spider.class);
      assert bean.getTypes().contains(Animal.class);
      assert bean.getTypes().contains(Object.class);
      assert bean.getTypes().contains(DeadlySpider.class);
      assert bean.getTypes().contains(DeadlyAnimal.class);
   }
   
   @Test
   @SpecAssertion(section = "2.2.3", id = "a")
   @SuppressWarnings("unused")
   public void testBeanClientCanCastBeanInstanceToAnyBeanType()
   {
      assert getBeans(Tarantula.class).size() == 1;
      Bean<Tarantula> bean = getBeans(Tarantula.class).iterator().next();
      Tarantula tarantula = getCurrentManager().getContext(bean.getScope()).get(bean);
      
      Spider spider = tarantula;
      Animal animal = tarantula;
      Object obj = tarantula;
      DeadlySpider deadlySpider = tarantula;
      DeadlyAnimal deadlyAnimal = tarantula;
   }
   
   @Test
   @SpecAssertion(section = "2.2.1", id = "c")
   public void testAbstractApiType()
   {
      assert getBeans(FriendlyAntelope.class).size() == 1;
      Bean<FriendlyAntelope> bean = getBeans(FriendlyAntelope.class).iterator().next();
      assert bean.getTypes().size() == 4;
      assert bean.getTypes().contains(FriendlyAntelope.class);
      assert bean.getTypes().contains(AbstractAntelope.class);
      assert bean.getTypes().contains(Animal.class);
      assert bean.getTypes().contains(Object.class);
   }
   
   @Test
   @SpecAssertion(section = "2.2.1", id = "d")
   public void testFinalApiType()
   {
      assert !getBeans(DependentFinalTuna.class).isEmpty();
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.1.3", id = "bd"),
      @SpecAssertion(section = "11.1", id = "ba")
   })
   public void testMultipleStereotypes()
   {
      Bean<ComplicatedTuna> tunaBean = getBeans(ComplicatedTuna.class).iterator().next();
      assert tunaBean.getScope().equals(RequestScoped.class);
      assert tunaBean.getName().equals("complicatedTuna");
   }
   
   @Test
   @SpecAssertion(section = "3.1.3", id = "c")
   public void testBeanExtendsAnotherBean()
   {
      assert !getBeans(Spider.class).isEmpty();
      assert !getBeans(Tarantula.class).isEmpty();
   }
   
   @Test
   @SpecAssertion(section = "11.1", id = "bb")
   public void testBeanClassOnSimpleBean()
   {
      Set<Bean<Horse>> beans = getBeans(Horse.class);
      assert beans.size() == 1;
      assert beans.iterator().next().getBeanClass().equals(Horse.class);
   }
}