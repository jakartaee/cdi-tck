package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

class FishPond
{
   public boolean postConstructCalled;
   
   public Animal goldfish;
   
   public Goose goose;
   
   @Inject
   public Salmon salmon;
   
   @Inject
   public FishPond(Goldfish goldfish, Goose goose)
   {
      this.goldfish = goldfish;
      this.goose = goose;
   }
   
   @PostConstruct
   public void postConstruct()
   {
      postConstructCalled = true;
   }
   
   @PreDestroy
   public void destroy()
   {
      assert !Salmon.isBeanDestroyed();
   }
}
