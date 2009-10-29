package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution;

import javax.enterprise.util.AnnotationLiteral;

abstract class ExpensiveLiteral extends AnnotationLiteral<Expensive> implements Expensive
{
   
}
