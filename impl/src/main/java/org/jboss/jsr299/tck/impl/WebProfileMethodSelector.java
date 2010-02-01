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
package org.jboss.jsr299.tck.impl;

import java.util.Arrays;
import java.util.List;

import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.IMethodSelector;
import org.testng.IMethodSelectorContext;
import org.testng.ITestNGMethod;

public class WebProfileMethodSelector implements IMethodSelector
{

   private static final long serialVersionUID = 4868366080536037160L;

   public boolean includeMethod(IMethodSelectorContext ctx, ITestNGMethod method, boolean isTestMethod)
   {
      if (isWar(method.getMethod().getDeclaringClass()) && !isFullProfileOnly(method.getGroups()))
      {
         return true;
      }
      else
      {
         ctx.setStopped(true);
         return false;
      }
   }

   public void setTestMethods(List<ITestNGMethod> arg0)
   {
      // No-op, not needed
   }
   
   private static boolean isWar(Class<?> declaringClass)
   {
      if (declaringClass.isAnnotationPresent(Packaging.class))
      {
         return declaringClass.getAnnotation(Packaging.class).value().equals(PackagingType.WAR);
      }
      else
      {
         // WAR is default
         return true;
      }
   }
   
   private boolean isFullProfileOnly(String[] groups)
   {
      return Arrays.asList(groups).contains("javaee-full-only");
   }

}
