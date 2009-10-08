package org.jboss.jsr299.tck.tests.context.passivating.broken.passivatingManagedBeanWithNonPassivatingBeanConstructor;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

@SessionScoped
class Loviisa_Broken extends City implements Serializable
{
   private static final long serialVersionUID = -2866858442948392508L;

   public Loviisa_Broken() {
   }
   
   @Inject
   public Loviisa_Broken(Violation reference) {
      
   }
}
