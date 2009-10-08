package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import javax.ejb.Stateless;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.jboss.jsr299.tck.literals.NewLiteral;

@Stateless
public class ExplicitConstructorSessionBean implements ExplicitConstructor
{

   private static int constructorCalls = 0;
   private static SimpleBean injectedSimpleBean;
   
   public static final New NEW = new NewLiteral()
   {
      
      public Class<?> value()
      {
         return ExplicitConstructorSessionBean.class;
      }
   };
   
   @Inject
   public ExplicitConstructorSessionBean(SimpleBean bean)
   {
      constructorCalls++;
      injectedSimpleBean = bean;
   }

   public int getConstructorCalls()
   {
      return constructorCalls;
   }

   public void setConstructorCalls(int numCalls)
   {
      constructorCalls = numCalls;
   }

   public SimpleBean getInjectedSimpleBean()
   {
      return injectedSimpleBean;
   }

   public void setInjectedSimpleBean(SimpleBean injectedSimpleBean)
   {
      ExplicitConstructorSessionBean.injectedSimpleBean = injectedSimpleBean;
   }

}
