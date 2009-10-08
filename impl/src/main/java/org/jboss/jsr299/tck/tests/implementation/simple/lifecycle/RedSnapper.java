package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.enterprise.context.RequestScoped;

@FishStereotype
@RequestScoped
class RedSnapper implements Animal
{
   
   private boolean touched;
   
   public void ping()
   {
      this.touched = true;
   }
   
   /**
    * @return the touched
    */
   public boolean isTouched()
   {
      return touched;
   }
   
}
