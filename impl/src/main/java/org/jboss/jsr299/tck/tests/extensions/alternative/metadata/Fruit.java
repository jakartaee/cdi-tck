package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

import javax.enterprise.inject.spi.InjectionPoint;

interface Fruit
{
   InjectionPoint getMetadata();
}
