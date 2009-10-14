package org.jboss.jsr299.tck.tests.extensions.annotated;

import javax.enterprise.inject.spi.InjectionPoint;

interface Fruit
{
   InjectionPoint getMetadata();
}
