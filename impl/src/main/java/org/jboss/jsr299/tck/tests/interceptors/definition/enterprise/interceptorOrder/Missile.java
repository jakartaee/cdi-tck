package org.jboss.jsr299.tck.tests.interceptors.definition.enterprise.interceptorOrder;

import javax.ejb.Stateful;
import javax.interceptor.Interceptors;

@Stateful
@Airborne
@Interceptors(RadarInterceptor.class)
public class Missile implements MissileLocal
{
   public void fire() {}
}