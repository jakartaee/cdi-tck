package org.jboss.jsr299.tck.tests.context.passivating.broken.passivatingManagedBeanWithNonPassivatingInitializerMethod;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

@SessionScoped
class Forssa_Broken implements Serializable
{
   private static final long serialVersionUID = 2155797154677120837L;

   public Forssa_Broken() {
   }
   
   @Inject
   public Forssa_Broken(Violation reference) {
   }
   
}
