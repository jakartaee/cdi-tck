package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution;

import javax.enterprise.inject.AnnotationLiteral;

class ChunkyLiteral extends AnnotationLiteral<Chunky> implements Chunky
{
   public boolean realChunky()
   {
      return true;
   }
}
