package org.jboss.jsr299.tck.tests.event.resolve.typeWithParameters;

class Foo<T>
{
   private boolean observed;
   
   public boolean isObserved()
   {
      return observed;
   }
   
   public void setObserved(boolean observed)
   {
      assert !this.observed : "Event should only be observed once";
      this.observed = observed;
   }
}
