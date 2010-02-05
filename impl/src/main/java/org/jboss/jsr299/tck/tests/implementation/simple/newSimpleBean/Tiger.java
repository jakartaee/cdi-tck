package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.New;

import org.jboss.jsr299.tck.literals.NewLiteral;

@Alternative
public class Tiger
{

   public static final New NEW = new NewLiteral()
   {
      
      public Class<?> value()
      {
         return Tiger.class;
      }
   };
   
}
