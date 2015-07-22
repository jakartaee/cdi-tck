package org.jboss.cdi.tck.tests.event.observer.async.handlingExceptions;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.enterprise.event.Observes;

public class LondonRadioStation {
    public static AtomicBoolean observed = new AtomicBoolean(false);
    public static AtomicReference<Exception> exception;

    public void observe(@Observes RadioMessage radioMessage) throws Exception {
        observed.set(true);
        exception = new AtomicReference<>(new IllegalStateException(LondonRadioStation.class.getName()));
        throw exception.get();
    }
}
