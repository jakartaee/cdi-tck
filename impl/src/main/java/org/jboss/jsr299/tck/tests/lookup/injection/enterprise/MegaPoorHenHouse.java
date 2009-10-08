package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import javax.ejb.Stateless;

@Stateless
public class MegaPoorHenHouse extends PoorHenHouse implements MegaPoorHenHouseLocal
{
   public Fox getFox()
   {
      return fox;
   }
}
