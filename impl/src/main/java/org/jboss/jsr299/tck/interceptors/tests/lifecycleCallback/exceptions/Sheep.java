package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback.exceptions;

import javax.annotation.PostConstruct;

class Sheep
{
   @PostConstruct
   public void postConstruct()
   {
      throw new RuntimeException();
   }
}
