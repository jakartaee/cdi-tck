package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.annotation.PreDestroy;
import javax.inject.Current;
import javax.inject.Initializer;

class FishPond
{
   
   public Animal goldfish;
   
   @Current
   public Salmon salmon;
   
   @Initializer
   public FishPond(Goldfish goldfish)
   {
      this.goldfish = goldfish;
   }
   
   @PreDestroy
   public void destroy()
   {
      assert !Salmon.isBeanDestroyed();
   }
}
