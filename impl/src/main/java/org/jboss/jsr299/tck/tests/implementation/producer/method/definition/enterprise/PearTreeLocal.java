package org.jboss.jsr299.tck.tests.implementation.producer.method.definition.enterprise;

import javax.ejb.Local;

@Local
public interface PearTreeLocal
{
   Pear produceLightYellowPear();
}
