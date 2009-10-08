package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import javax.ejb.Local;
import javax.enterprise.inject.New;

import org.jboss.jsr299.tck.literals.NewLiteral;

@Local
public interface InitializerSimpleBeanLocal
{
   public static final New NEW = new NewLiteral()
   {
      
      public Class<?> value()
      {
         return InitializerSimpleBean.class;
      }
   };
   
   public void initializer();
   public void businessMethod();
   public int getInitializerCalls();
   public void setInitializerCalls(int initializerCalls);
}
