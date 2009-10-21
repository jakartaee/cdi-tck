package org.jboss.jsr299.tck.tests.interceptors.definition;

import javax.inject.Inject;

@MissileBinding
class Missile
{
   @Inject
   private Warhead warhead;
   
   boolean initCalled = false;
   
   @Inject
   public void init() { 
       initCalled = true;
   }
   
   public boolean initCalled() {
      return initCalled;
   }
   
   public void fire() { }

   public Warhead getWarhead()
   {
      return warhead;
   }
}
