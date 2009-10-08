package org.jboss.jsr299.tck.tests.interceptors.definition.multipleBindings;

import javax.interceptor.Interceptors;

@Interceptors(MissileInterceptor.class)
@Fast @Deadly
class FastAndDeadlyMissile implements Missile
{
   public void fire() {}
}
