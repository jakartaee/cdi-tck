package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

class Milk
{
   @SuppressWarnings("unused")
   private boolean fresh;

   public Milk(boolean fresh)
   {
      this.fresh = fresh;
   }
}
