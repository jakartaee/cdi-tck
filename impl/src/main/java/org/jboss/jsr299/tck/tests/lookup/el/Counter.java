package org.jboss.jsr299.tck.tests.lookup.el;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class Counter
{
   private int count = 0;
   
   public void add() {
      count++;
   }
   
   public int getCount() {
      return count;
   }
}
