package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution;

import javax.enterprise.inject.AnnotationLiteral;

abstract class ExpensiveLiteral extends AnnotationLiteral<Expensive> implements Expensive
{
   
}
