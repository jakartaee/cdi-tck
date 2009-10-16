package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingInitializerParameterInInterceptor;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.interceptor.AroundInvoke;

import org.jboss.testharness.impl.packaging.jsr299.BeansXml;

@Interceptor @City
@BeansXml("beans.xml")
class BrokenInterceptor implements Serializable
{
   @Inject
   public void init(District district) {}

   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }
}
