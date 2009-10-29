package org.jboss.jsr299.tck.tests.event.resolve.binding;

import javax.enterprise.util.AnnotationLiteral;

class BindingTypeCBinding extends AnnotationLiteral<BindingTypeC> implements BindingTypeC
{
   private String value;

   public BindingTypeCBinding(String value)
   {
      this.value = value;
   }

   public String value()
   {
      return this.value;
   }
}
