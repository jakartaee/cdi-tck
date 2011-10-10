package org.jboss.jsr299.tck.tests.interceptors.definition.inheritance;

import javax.ejb.Stateless;

@GuardedBySquirrel
@GuardedByWoodpecker
@Stateless
public class Flower extends Plant {

    public void pong() {
    }

}
