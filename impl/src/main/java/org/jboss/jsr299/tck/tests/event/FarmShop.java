package org.jboss.jsr299.tck.tests.event;

import javax.enterprise.inject.Specializes;

@Specializes
class FarmShop extends Shop
{
   
   @Override
   public String getClassName()
   {
      return FarmShop.class.getName();
   }
   
}
