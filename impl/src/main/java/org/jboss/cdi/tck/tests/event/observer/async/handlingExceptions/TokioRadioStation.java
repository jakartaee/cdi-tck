package org.jboss.cdi.tck.tests.event.observer.async.handlingExceptions;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.event.Observes;

public class TokioRadioStation {

    public static AtomicBoolean observed = new AtomicBoolean(false);

    public void observe(@Observes RadioMessage radioMessage){
        observed.set(true);
    }
}
