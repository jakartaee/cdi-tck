package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

class OrderProcessor
{
   
   public static boolean postConstructCalled = false;
   
   public static boolean preDestroyCalled = true;
   
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
   
   public void order()
   {
      
   }
    
   
}
