package org.jboss.cdi.tck.tests.event.lifecycle;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.BeforeDestroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Shutdown;
import jakarta.enterprise.event.Startup;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ObservingBean {

    public static List<String> OBSERVED_STARTING_EVENTS = new ArrayList<>();
    public static List<String> OBSERVED_SHUTDOWN_EVENTS = new ArrayList<>();

    public void startup(@Observes Startup startup) {
        OBSERVED_STARTING_EVENTS.add(Startup.class.getSimpleName());
    }

    public void initAppScope(@Observes @Initialized(ApplicationScoped.class) Object init) {
        OBSERVED_STARTING_EVENTS.add(ApplicationScoped.class.getSimpleName());
    }

    public void shutdown(@Observes Shutdown shutdown) {
        OBSERVED_SHUTDOWN_EVENTS.add(Shutdown.class.getSimpleName());
    }

    public void observeBeforeShutdown(@Observes @BeforeDestroyed(ApplicationScoped.class) Object event) {
        OBSERVED_SHUTDOWN_EVENTS.add(ApplicationScoped.class.getSimpleName());
    }
}
