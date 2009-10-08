package org.jboss.jsr299.tck.tests.interceptors.definition.broken.invalidLifecycleInterceptor;

import javax.inject.Inject;

class Foo
{
   @Transactional @Inject void init()
   {
      
   }
}
