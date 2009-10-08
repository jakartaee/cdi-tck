package org.jboss.jsr299.tck.tests.interceptors.definition.multipleBindings;

import javax.interceptor.Interceptors;

@Interceptors(MissileInterceptor.class)
@Slow @Deadly
class SlowMissile implements Missile
{
   public void fire() { }
}
