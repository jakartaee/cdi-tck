package org.jboss.jsr299.tck.tests.implementation.producer.method.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

class Web
{
   private Boolean destroyed;
   
   public boolean isSpun()
   {
      return destroyed != null;
   }
   
   public boolean isDestroyed()
   {
      return Boolean.TRUE.equals(destroyed);
   }
   
   @PostConstruct public void spin()
   {
      destroyed = false;
   }
   
   @PreDestroy public void destroy()
   {
      destroyed = true;
   }
}
