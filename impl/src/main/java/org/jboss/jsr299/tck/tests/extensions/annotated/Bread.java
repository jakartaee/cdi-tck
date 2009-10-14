package org.jboss.jsr299.tck.tests.extensions.annotated;

class Bread
{
   @SuppressWarnings("unused")
   private boolean fresh;
   
   public Bread(boolean fresh) {
      this.fresh = fresh;
   }
}
