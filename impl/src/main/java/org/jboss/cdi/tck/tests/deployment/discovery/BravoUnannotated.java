package org.jboss.cdi.tck.tests.deployment.discovery;

// no bean defining annotation
public class BravoUnannotated implements Ping{

    @Override
    public void pong() {
    }
}
