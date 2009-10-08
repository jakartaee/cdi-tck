package org.jboss.jsr299.tck.tests.extensions.producer;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.inject.spi.Producer;

public class ProducerProcessor implements Extension
{
   private static InjectionTarget<Cat> catInjectionTarget;
   private static Producer<Dog> noisyDogProducer;
   private static Producer<Dog> quietDogProducer;
   private static InjectionTarget<Dog> dogInjectionTarget;
   private static AnnotatedType<Dog> dogAnnotatedType;

   public void processDogProducerProducer(@Observes ProcessProducer<DogProducer, Dog> producerEvent)
   {
      if (producerEvent.getAnnotatedMember().isAnnotationPresent(Noisy.class))
      {
         noisyDogProducer = producerEvent.getProducer();
         assert producerEvent.getAnnotatedMember() instanceof AnnotatedMethod<?>;
      }
      else if (producerEvent.getAnnotatedMember().isAnnotationPresent(Quiet.class))
      {
         quietDogProducer = producerEvent.getProducer();
         assert producerEvent.getAnnotatedMember() instanceof AnnotatedField<?>;
      }
   }

   public void processCatProducer(@Observes ProcessInjectionTarget<Cat> producerEvent)
   {
      catInjectionTarget = producerEvent.getInjectionTarget();
   }

   public void processDogInjectionTarget(@Observes ProcessInjectionTarget<Dog> injectionTargetEvent)
   {
      // There a couple, but it does not matter which one is used for the tests
      dogInjectionTarget = injectionTargetEvent.getInjectionTarget();
      dogAnnotatedType = injectionTargetEvent.getAnnotatedType();
   }

   public static Producer<Dog> getNoisyDogProducer()
   {
      return noisyDogProducer;
   }

   public static Producer<Dog> getQuietDogProducer()
   {
      return quietDogProducer;
   }

   public static InjectionTarget<Cat> getCatInjectionTarget()
   {
      return catInjectionTarget;
   }

   public static InjectionTarget<Dog> getDogInjectionTarget()
   {
      return dogInjectionTarget;
   }

   public static AnnotatedType<Dog> getDogAnnotatedType()
   {
      return dogAnnotatedType;
   }
}
