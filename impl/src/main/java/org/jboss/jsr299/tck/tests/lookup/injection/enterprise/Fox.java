package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

@Dependent
public class Fox implements Serializable
{
   
   public String getName()
   {
      return "gavin";
   }
   
}
