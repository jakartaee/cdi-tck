package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback.exceptions;

import javax.annotation.PostConstruct;
import javax.interceptor.Interceptors;

@Interceptors(GoatInterceptor.class)
class Goat
{
   @PostConstruct
   public void postConstruct()
   {
      throw new IllegalStateException();
   }
}
