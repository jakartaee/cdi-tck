package org.jboss.jsr299.tck.tests.context;

import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;

class DummyContext implements Context
{
   public <T> T get(Contextual<T> bean, CreationalContext<T> creationalContext)
   {
      throw new UnsupportedOperationException();
   }

   public <T> T get(Contextual<T> contextual)
   {
      return get(contextual, null);
   }

   public Class<? extends Annotation> getScope()
   {
      return DummyScoped.class;
   }

   public boolean isActive()
   {
      return true;
   }
}