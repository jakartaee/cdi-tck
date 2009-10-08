package org.jboss.jsr299.tck.tests.event.observer;

import javax.enterprise.inject.AnnotationLiteral;

class RoleBinding extends AnnotationLiteral<Role> implements Role
{
   private String value = null;

   public RoleBinding(String value)
   {
      this.value = value;
   }

   public String value()
   {
      return value;
   }

}
