package org.jboss.jsr299.tck.tests.extensions.annotated;

import javax.enterprise.inject.AnnotationLiteral;

class ExpensiveLiteral extends AnnotationLiteral<Expensive> implements Expensive
{
}
