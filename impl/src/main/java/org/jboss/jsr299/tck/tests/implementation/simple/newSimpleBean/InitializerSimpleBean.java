package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.jsr299.tck.literals.NewLiteral;

@SessionScoped
@Named("Charlie") @Default
class InitializerSimpleBean implements Serializable
{
   
   public static final New NEW = new NewLiteral()
   {
      
      public Class<?> value()
      {
         return InitializerSimpleBean.class;
      }
      
   };
   
   private static final long serialVersionUID = 1L;
   private static int        initializerCalls = 0;

   @Inject
   protected Order           order;

   @Inject
   public void initializer()
   {
      initializerCalls++;
   }

   public void businessMethod()
   {
      
   }

   public static int getInitializerCalls()
   {
      return initializerCalls;
   }

   public static void setInitializerCalls(int initializerCalls)
   {
      InitializerSimpleBean.initializerCalls = initializerCalls;
   }
}
