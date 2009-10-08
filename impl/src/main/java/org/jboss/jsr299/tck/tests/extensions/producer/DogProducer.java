package org.jboss.jsr299.tck.tests.extensions.producer;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

class DogProducer
{
   public static final String QUIET_DOG_COLOR = "Black";
   public static final String NOISY_DOG_COLOR = "White";
   private static boolean noisyDogProducerCalled;
   private static boolean noisyDogDisposerCalled;
   
   @Produces @Quiet
   private Dog quietDog = new Dog(QUIET_DOG_COLOR);

   @Produces @Noisy
   public Dog produceNoisyDog(DogBed dogBed)
   {
      noisyDogProducerCalled = true;
      return new Dog(NOISY_DOG_COLOR);
   }

   public void disposeNoisyDog(@Disposes @Noisy Dog dog)
   {
      noisyDogDisposerCalled = true;
   }

   public static boolean isNoisyDogProducerCalled()
   {
      return noisyDogProducerCalled;
   }
   
   public static boolean isNoisyDogDisposerCalled()
   {
      return noisyDogDisposerCalled;
   }

   public static void reset()
   {
      DogProducer.noisyDogProducerCalled = false;
      DogProducer.noisyDogDisposerCalled = false;
   }
}
