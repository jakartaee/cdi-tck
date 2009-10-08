package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import javax.enterprise.inject.New;

import org.jboss.jsr299.tck.literals.NewLiteral;

@Tame
class Lion
{
   
   public static final New NEW = new NewLiteral()
   {
      
      public Class<?> value()
      {
         return Lion.class;
      }
   };
   
}
