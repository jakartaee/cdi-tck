package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import javax.enterprise.inject.New;
import javax.inject.Inject;

public class LionCage
{
   
   @Inject @New
   private Lion newLion;
   
   public Lion getNewLion()
   {
      return newLion;
   }

}
