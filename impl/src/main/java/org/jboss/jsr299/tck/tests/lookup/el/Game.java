package org.jboss.jsr299.tck.tests.lookup.el;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class Game
{
   @Inject
   public void initialize(Counter counter) {
      counter.add();
   }
   
   public String getValue() 
   {
      return "foo";
   }
   
}
