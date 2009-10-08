package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution.parameterized;

import java.util.HashMap;

import javax.inject.Inject;

class InjectedBean
{
   @Inject
   private HashMap<? extends Number, ? super Integer> map;

   public HashMap<? extends Number, ? super Integer> getMap()
   {
      return map;
   }
}
