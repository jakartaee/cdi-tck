package org.jboss.jsr299.tck.tests.extensions.producer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;


class Dog
{
   private static boolean     constructorCalled = false;
   private static boolean     postConstructCalled;
   private static boolean     preDestroyCalled;
   private static DogBed      dogBed;

   public static final String DEFAULT_COLOR     = "Brown";
   private String             color             = DEFAULT_COLOR;
   
   @Inject
   private DogBone            dogBone;

   public Dog()
   {
      constructorCalled = true;
   }

   public Dog(String color)
   {
      this.color = color;
   }

   @Inject
   public void init(DogBed dogBed)
   {
      Dog.dogBed = dogBed;
   }

   @PostConstruct
   public void postConstruct()
   {
      postConstructCalled = true;
   }

   @PreDestroy
   public void preDestroy()
   {
      preDestroyCalled = true;
   }

   public static boolean isConstructorCalled()
   {
      return constructorCalled;
   }

   public static void setConstructorCalled(boolean constructorCalled)
   {
      Dog.constructorCalled = constructorCalled;
   }

   public static DogBed getDogBed()
   {
      return dogBed;
   }

   public static void setDogBed(DogBed dogBed)
   {
      Dog.dogBed = dogBed;
   }

   public String getColor()
   {
      return color;
   }

   public static boolean isPostConstructCalled()
   {
      return postConstructCalled;
   }

   public static void setPostConstructCalled(boolean postConstructCalled)
   {
      Dog.postConstructCalled = postConstructCalled;
   }

   public static boolean isPreDestroyCalled()
   {
      return preDestroyCalled;
   }

   public static void setPreDestroyCalled(boolean preDestroyCalled)
   {
      Dog.preDestroyCalled = preDestroyCalled;
   }

   public DogBone getDogBone()
   {
      return dogBone;
   }

   public void setDogBone(DogBone dogBone)
   {
      this.dogBone = dogBone;
   }
}
