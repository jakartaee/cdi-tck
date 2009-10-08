package org.jboss.jsr299.tck.tests.interceptors.definition;

import javax.inject.Inject;

@MissileBinding
class Missile
{
   @Inject
   public void init() { }
   
   public void fire() { }
}
