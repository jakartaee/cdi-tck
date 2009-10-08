package org.jboss.jsr299.tck.literals;

import javax.enterprise.inject.AnnotationLiteral;
import javax.inject.Named;

public class NamedLiteral extends AnnotationLiteral<Named> implements Named
{
   
   public String value()
   {
      return "";
   }
   
}
