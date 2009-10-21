package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class ProcessAnnotatedTypeObserver implements Extension
{
   
   public void observeGroceryAnnotatedType(@Observes ProcessAnnotatedType<Grocery> event) {
      event.setAnnotatedType(new GroceryWrapper(event.getAnnotatedType()));
   }
}
