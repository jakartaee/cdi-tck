package org.jboss.jsr299.tck.tests.implementation.producer.field.definition.broken.parameterizedReturnTypeWithWildcard;

import javax.enterprise.inject.Produces;

public class SpiderProducerWildCardType_Broken
{
   @Produces public FunnelWeaver<?> getAnotherFunnelWeaver = new FunnelWeaver<Object>();

}
