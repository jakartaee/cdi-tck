package org.jboss.jsr299.tck.tests.context.dependent;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

class Stable
{
   
   @Inject Horse horse;
   
   public static boolean destroyed;
   
   @PreDestroy
   public void preDestroy()
   {
      destroyed = true;
   }
   
}
