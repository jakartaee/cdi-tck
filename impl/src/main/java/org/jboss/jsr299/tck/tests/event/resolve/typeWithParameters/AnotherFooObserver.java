package org.jboss.jsr299.tck.tests.event.resolve.typeWithParameters;

import javax.enterprise.event.Observes;

class AnotherFooObserver
{
   public void observe(@Observes Foo<Integer> foo)
   {
      foo.setObserved(true);
   }
}
