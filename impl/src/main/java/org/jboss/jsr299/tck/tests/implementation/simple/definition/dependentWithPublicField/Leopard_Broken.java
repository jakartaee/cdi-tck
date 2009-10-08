package org.jboss.jsr299.tck.tests.implementation.simple.definition.dependentWithPublicField;

import javax.enterprise.context.RequestScoped;

@RequestScoped
class Leopard_Broken
{
   
   public String name = "pete";
   
}
