package org.jboss.jsr299.tck.tests.context.dependent;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
class Farm
{
   
   @Inject Stable stable;
   
   public void open() {};
   
}
