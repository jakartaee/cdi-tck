package org.jboss.jsr299.tck.tests.extensions.producer;

import javax.enterprise.context.spi.CreationalContext;

class CreationalContextImpl<T> implements CreationalContext<T>
{

   public void push(T incompleteInstance)
   {
   }

   public void release()
   {
   }

}
