package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution;

import javax.enterprise.inject.BeanTypes;

@BeanTypes(Canary.class)
public class Canary implements Bird
{

}
