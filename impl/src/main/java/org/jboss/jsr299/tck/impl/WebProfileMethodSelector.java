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
      return isWar(method.getMethod().getDeclaringClass()) && !isFullProfileOnly(method.getGroups());
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
      return Arrays.asList(groups).contains("javaee-full");
   }

}
