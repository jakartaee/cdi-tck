package org.jboss.jsr299.tck.tests.lookup.injection;

import javax.inject.Inject;

class SpiderNest
{
   
   public Integer numberOfSpiders;
   
   @Inject
   public SpiderNest(Integer numberOfSpiders)
   {
      this.numberOfSpiders = numberOfSpiders; 
   }
   
}
