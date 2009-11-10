package org.jboss.jsr299.tck.tests.interceptors.definition.enterprise.nonContextualReference;

import javax.ejb.Stateful;

@Stateful
@Airborne
public class Missile implements MissileLocal
{
   public void fire() {}
}