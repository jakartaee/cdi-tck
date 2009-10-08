package org.jboss.jsr299.tck.tests.implementation.simple.resource.env;

import javax.inject.Inject;

class GreetingBean
{
   @Inject @Greeting 
   private String phrase;
   
   public String greet()
   {
      return phrase;
   }
}
