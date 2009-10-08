package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import javax.ejb.Stateless;

@Stateless
public class DeluxeHenHouse extends HenHouse implements DeluxeHenHouseLocal
{
   public Fox getFox()
   {
      return fox;
   }
}
