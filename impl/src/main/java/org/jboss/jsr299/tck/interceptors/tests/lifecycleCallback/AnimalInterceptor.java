/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class AnimalInterceptor
{
   private static Set<Class<?>> postConstructInterceptorCalledFor = new HashSet<Class<?>>();
   private static Set<Class<?>> preDestroyInterceptorCalledFor = new HashSet<Class<?>>();

   @PostConstruct
   public void postConstruct(InvocationContext ctx)
   {
      postConstructInterceptorCalledFor.add(ctx.getTarget().getClass());
      try
      {
         ctx.proceed();
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception
   {
      if (ctx.getMethod().getName().equals("echo"))
      {
         return ctx.proceed() + ctx.getParameters()[0].toString();
      }
      else
      {
         return ctx.proceed();
      }
   }

   @PreDestroy
   public void preDestroy(InvocationContext ctx)
   {
      preDestroyInterceptorCalledFor.add(ctx.getTarget().getClass());
      try
      {
         ctx.proceed();
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   public static boolean isPostConstructInterceptorCalled(Class<?> clazz)
   {
      return postConstructInterceptorCalledFor.contains(clazz);
   }

   public static boolean isPreDestroyInterceptorCalled(Class<?> clazz)
   {
      return preDestroyInterceptorCalledFor.contains(clazz);
   }
}
