package org.jboss.jsr299.tck.tests.extensions.producer;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.Producer;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.testng.annotations.Test;

/**
 * Producer extension tests.
 * 
 * @author David Allen
 *
 */
@Artifact
@Resources({
   @Resource(source="javax.enterprise.inject.spi.Extension", destination="WEB-INF/classes/META-INF/services/javax.enterprise.inject.spi.Extension")
})
// Must be an integration test as it needs a resource copied to a folder
@IntegrationTest
@SpecVersion(spec="cdi", version="PFD2")
// TODO All tests fall under WBRI-345 for now, please create new issues for any failing tests after WBRI-345 is done
public class ProducerTest extends AbstractJSR299Test
{
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.2", id = "ba")
   })
   public void testProduceCallsInitializerAndConstructor()
   {
      InjectionTarget<Cat> injectionTarget = ProducerProcessor.getCatInjectionTarget();
      Bean<Cat> catBean = getUniqueBean(Cat.class);
      Cat.reset();
      injectionTarget.produce(getCurrentManager().createCreationalContext(catBean));
      assert Cat.isConstructorCalled();
      assert Cat.isInitializerCalled();
   }

   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.2", id = "c")
   })
   public void testDisposeDoesNothing()
   {
      InjectionTarget<Cat> injectionTarget = ProducerProcessor.getCatInjectionTarget();
      
      Cat cat = getInstanceByType(Cat.class);
      injectionTarget.dispose(cat);
      // The instance should still be available
      cat.ping();
   }

   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.2", id = "da")
   })
   public void testGetInjectionPointsForFields()
   {
      InjectionTarget<Cat> injectionTarget = ProducerProcessor.getCatInjectionTarget();
      assert injectionTarget.getInjectionPoints().size() == 3;
      boolean injectionPointPresent = false;
      for (InjectionPoint injectionPoint : injectionTarget.getInjectionPoints())
      {
         if (injectionPoint.getType().equals(CatFoodDish.class))
         {
            injectionPointPresent = true;
         }
      }
      assert injectionPointPresent;
   }

   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.2", id = "db")
   })
   public void testGetInjectionPointsForConstructorAndInitializer()
   {
      InjectionTarget<Cat> injectionTarget = ProducerProcessor.getCatInjectionTarget();
      assert injectionTarget.getInjectionPoints().size() == 3;
      boolean dogIPPresent = false;
      for (InjectionPoint injectionPoint : injectionTarget.getInjectionPoints())
      {
         if (injectionPoint.getType().equals(Dog.class))
         {
            dogIPPresent = true;
         }
      }
      assert dogIPPresent;
   }

   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.2", id = "dc")
   })
   public void testGetInjectionPointsForInitializer()
   {
      InjectionTarget<Cat> injectionTarget = ProducerProcessor.getCatInjectionTarget();
      assert injectionTarget.getInjectionPoints().size() == 3;
      boolean injectionPointPresent = false;
      for (InjectionPoint injectionPoint : injectionTarget.getInjectionPoints())
      {
         if (injectionPoint.getType().equals(Bird.class))
         {
            injectionPointPresent = true;
         }
      }
      assert injectionPointPresent;
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.2", id = "eaa"),
      @SpecAssertion(section = "11.5.7", id = "aa"),
      @SpecAssertion(section = "11.5.7", id = "ba"),
      @SpecAssertion(section = "11.5.7", id = "ca"),
      @SpecAssertion(section = "11.5.7", id = "da"),
      @SpecAssertion(section = "12.3", id = "ha")
   })
   public void testProduceCallsProducerMethod()
   {
      Producer<Dog> producer = ProducerProcessor.getNoisyDogProducer();
      Bean<Dog> dogBean = getUniqueBean(Dog.class, new AnnotationLiteral<Noisy>() {});
      DogProducer.reset();
      Dog dog = producer.produce(getCurrentManager().createCreationalContext(dogBean));
      assert DogProducer.isNoisyDogProducerCalled();
      assert dog.getColor().equals(DogProducer.NOISY_DOG_COLOR);
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.2", id = "eba"),
      @SpecAssertion(section = "11.5.7", id = "ab"),
      @SpecAssertion(section = "11.5.7", id = "bb"),
      @SpecAssertion(section = "11.5.7", id = "cb"),
      @SpecAssertion(section = "11.5.7", id = "db"),
      @SpecAssertion(section = "12.3", id = "hb")
   })
   public void testProduceAccessesProducerField()
   {
      Producer<Dog> producer = ProducerProcessor.getQuietDogProducer();
      Bean<Dog> dogBean = getUniqueBean(Dog.class, new AnnotationLiteral<Quiet>() {});
      DogProducer.reset();
      Dog dog = producer.produce(getCurrentManager().createCreationalContext(dogBean));
      assert dog.getColor().equals(DogProducer.QUIET_DOG_COLOR);
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.2", id = "faa")
   })
   public void testProducerForMethodDisposesProduct()
   {
      
      Bean<Dog> dogBean = getUniqueBean(Dog.class, new AnnotationLiteral<Noisy>() {});
      Producer<Dog> producer = ProducerProcessor.getNoisyDogProducer();
      DogProducer.reset();
      Dog dog = producer.produce(getCurrentManager().createCreationalContext(dogBean));
      assert DogProducer.isNoisyDogProducerCalled();
      producer.dispose(dog);
      assert DogProducer.isNoisyDogDisposerCalled();
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.2", id = "g")
   })
   public void testInjectionPointsForProducerMethod()
   {
      Producer<Dog> producer = ProducerProcessor.getNoisyDogProducer();
      DogProducer.reset();
      assert producer.getInjectionPoints().size() == 1;
      assert producer.getInjectionPoints().iterator().next().getType().equals(DogBed.class);
   }

   @SuppressWarnings("unchecked")
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.2", id = "i"),
      @SpecAssertion(section = "11.5.6", id = "ba"),
      @SpecAssertion(section = "12.3", id = "ba")
   })
   public void testInjectionTargetInject()
   {
      InjectionTarget<Dog> injectionTarget = ProducerProcessor.getDogInjectionTarget();
      Bean<Dog> dogBean = (Bean<Dog>) getCurrentManager().getBeans(Dog.class).iterator().next();
      CreationalContext<Dog> dogCreationalContext = getCurrentManager().createCreationalContext(dogBean);
      Dog dog = dogBean.create(dogCreationalContext);
      dog.setDogBone(null);
      injectionTarget.inject(dog, dogCreationalContext);
      assert dog.getDogBone() != null;
   }

   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.2", id = "j")
   })
   public void testInjectionTargetPostConstruct()
   {
      InjectionTarget<Dog> injectionTarget = ProducerProcessor.getDogInjectionTarget();
      Dog dog = getInstanceByType(Dog.class, new AnnotationLiteral<Noisy>() {});
      Dog.setPostConstructCalled(false);
      injectionTarget.postConstruct(dog);
      assert Dog.isPostConstructCalled();
   }

   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.2", id = "k")
   })
   public void testInjectionTargetPreDestroy()
   {
      InjectionTarget<Dog> injectionTarget = ProducerProcessor.getDogInjectionTarget();
      Dog dog = getInstanceByType(Dog.class, new AnnotationLiteral<Noisy>() {});
      Dog.setPreDestroyCalled(false);
      injectionTarget.preDestroy(dog);
      assert Dog.isPreDestroyCalled();
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "bb"),
      @SpecAssertion(section = "11.5.6", id = "ea")
   })
   public void testSettingInjectionTargetReplacesIt()
   {
      BirdCageInjectionTarget.setInjectCalled(false);
      getInstanceByType(BirdCage.class);
      assert BirdCageInjectionTarget.isInjectCalled();
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aba")
   })
   public void testGetAnnotatedTypeOnProcessInjectionTarget()
   {
      assert ProducerProcessor.getDogAnnotatedType() != null;
      assert ProducerProcessor.getDogAnnotatedType().getBaseType().equals(Dog.class);
   }
}
