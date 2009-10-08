package org.jboss.jsr299.tck.tests.context.dependent;

import javax.annotation.PreDestroy;

class Horse
{
   
   public static boolean destroyed;
   
   @PreDestroy
   public void preDestroy()
   {
      destroyed = true;
   }
   
}
