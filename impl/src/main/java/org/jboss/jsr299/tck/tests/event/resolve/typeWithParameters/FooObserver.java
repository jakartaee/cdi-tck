package org.jboss.jsr299.tck.tests.event.resolve.typeWithParameters;

import javax.enterprise.event.Observes;

class FooObserver
{
   public void observe(@Observes Foo<String> foo)
   {
      foo.setObserved(true);
   }
}
