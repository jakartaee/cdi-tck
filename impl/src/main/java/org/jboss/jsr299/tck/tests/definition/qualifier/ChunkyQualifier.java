package org.jboss.jsr299.tck.tests.definition.qualifier;

import javax.enterprise.util.AnnotationLiteral;

class ChunkyQualifier extends AnnotationLiteral<Chunky> implements Chunky
{
   private boolean chunky;

   public ChunkyQualifier(boolean chunky)
   {
      this.chunky = chunky;
   }

   public boolean realChunky()
   {
      return chunky;
   }

}
