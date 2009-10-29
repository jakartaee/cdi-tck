package org.jboss.jsr299.tck.tests.definition.qualifier.enterprise;

import javax.enterprise.util.AnnotationLiteral;

public class HairyQualifier extends AnnotationLiteral<Hairy> implements Hairy
{
   private boolean clipped;
   
   public HairyQualifier(boolean clipped)
   {
      this.clipped = clipped;
   }
   
   public boolean clipped()
   {
      return clipped;
   }

}