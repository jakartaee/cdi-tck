package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import java.io.Serializable;

import javax.enterprise.inject.New;

import org.jboss.jsr299.tck.literals.NewLiteral;

class Order implements Serializable
{

   public static final New NEW = new NewLiteral()
   {
      
      public Class<?> value()
      {
         return Order.class;
      }
      
   };
   
   private static final long serialVersionUID = 1L;
   public static boolean     constructed      = true;

}
