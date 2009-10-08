package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.New;
import javax.inject.Named;

import org.jboss.jsr299.tck.literals.NewLiteral;

@SessionScoped
@Named("Fred") @Default
class ExplicitContructorSimpleBean implements Serializable
{
   private static final long serialVersionUID = 1L;
   private static int constructorCalls = 0;
   
   public static final New NEW = new NewLiteral()
   {
      
      public Class<?> value()
      {
         return ExplicitContructorSimpleBean.class;
      }
   };
   
   public ExplicitContructorSimpleBean() {
      constructorCalls++;
   }

   public static int getConstructorCalls()
   {
      return constructorCalls;
   }

   public static void setConstructorCalls(int constructorCalls)
   {
      ExplicitContructorSimpleBean.constructorCalls = constructorCalls;
   }

}
