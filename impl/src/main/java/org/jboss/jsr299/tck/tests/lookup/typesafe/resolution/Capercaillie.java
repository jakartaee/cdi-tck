package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution;

import javax.ejb.Stateless;
import javax.enterprise.inject.Typed;

@Stateless
@Typed(CapercaillieLocal.class)
public class Capercaillie implements CapercaillieLocal
{

}
