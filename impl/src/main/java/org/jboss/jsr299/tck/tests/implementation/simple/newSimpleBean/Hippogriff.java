package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.New;
import javax.inject.Inject;

public class Hippogriff
{
   
   private Map<String, String> homes;

   @Inject
   public Hippogriff(@New HashMap<String, String> homes)
   {
      this.homes = homes;
   }
   
   public Map<String, String> getHomes()
   {
      return homes;
   }

}
