package org.jboss.jsr299.tck.tests.decorators.definition.broken.observer;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.jboss.jsr299.tck.tests.interceptors.definition.broken.observer.FooPayload;

@Decorator
public class FilesystemLogger implements Logger {

    @Inject
    @Any
    @Delegate
    Logger logger;

    public void log() {
        logger.log();
    }

    public void observeFoo(@Observes FooPayload fooPayload) {
    }

}
