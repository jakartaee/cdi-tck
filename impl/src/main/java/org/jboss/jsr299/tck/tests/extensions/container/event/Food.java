package org.jboss.jsr299.tck.tests.extensions.container.event;

class Food
{
   @SuppressWarnings("unused")
   private boolean fresh;
   
   public Food(boolean fresh) {
      this.fresh = fresh;
   }
}
