package org.jboss.jsr299.tck.tests.context.dependent.ejb;

import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;


@RequestScoped @Stateful
public class Farm implements FarmLocal
{
   
   public static boolean destroyed;
   
   @Inject Stable stable;
   
   public void open() {}
   
   
   @PreDestroy
   public void preDestroy()
   {
      Farm.destroyed = true;
   }
   
}
