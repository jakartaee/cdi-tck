package org.jboss.jsr299.tck.tests.event;

import javax.enterprise.util.AnnotationLiteral;


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
