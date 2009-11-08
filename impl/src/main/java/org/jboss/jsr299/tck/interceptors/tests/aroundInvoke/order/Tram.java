package org.jboss.jsr299.tck.interceptors.tests.aroundInvoke.order;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;

@Interceptors( { Interceptor1.class, Interceptor3.class })
class Tram extends RailVehicle
{
   @Interceptors( { Interceptor4.class, Interceptor5.class })
   public int getId()
   {
      return 0;
   }
   
   @AroundInvoke
   public Object intercept3(InvocationContext ctx) throws Exception
   {
      int id = (Integer) ctx.proceed();
      assert id == 0;
      return id + 1;
   }
}
