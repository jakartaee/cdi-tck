package org.jboss.jsr299.tck.tests.context.passivating.broken.passivatingManagedBeanWithNonPassivatingInjcetedField;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

@SessionScoped
class Vantaa_Broken implements Serializable
{
   private static final long serialVersionUID = -1686562136639336613L;
   
   @Inject
   private Violation reference;

   public Violation getReference()
   {
      return reference;
   }

   public void setReference(Violation reference)
   {
      this.reference = reference;
   }

   public String test() {
      return reference.toString();
   }
   
}
