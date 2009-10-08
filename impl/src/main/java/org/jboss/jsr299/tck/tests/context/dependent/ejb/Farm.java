package org.jboss.jsr299.tck.tests.context.dependent.ejb;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;


@RequestScoped @Stateful
public class Farm implements FarmLocal
{
   
   @Inject Stable stable;
   
   public void open() {};
   
}
