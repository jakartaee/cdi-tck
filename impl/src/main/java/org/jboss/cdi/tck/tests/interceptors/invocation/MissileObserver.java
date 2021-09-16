package org.jboss.cdi.tck.tests.interceptors.invocation;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;

@AlmightyBinding
@Dependent
public class MissileObserver {

    public static boolean observed = false;

    public void observeMissile(@Observes Missile missile) {
        observed = true;
    }

}
