package org.jboss.cdi.tck.tests.interceptors.invocation;

import javax.enterprise.event.Observes;

@MissileBinding
public class MissileObserver {

    public static boolean observed = false;

    public void observeMissile(@Observes Missile missile) {
        observed = true;
    }

}
