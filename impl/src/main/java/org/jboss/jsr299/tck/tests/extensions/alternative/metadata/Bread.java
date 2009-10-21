package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

class Bread
{
   @SuppressWarnings("unused")
   private boolean fresh;
   
   public Bread(boolean fresh) {
      this.fresh = fresh;
   }
}
