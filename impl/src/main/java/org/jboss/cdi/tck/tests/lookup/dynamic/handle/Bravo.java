package org.jboss.cdi.tck.tests.lookup.dynamic.handle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.cdi.tck.util.ActionSequence;

import java.util.UUID;

@ApplicationScoped
public class Bravo {

    private String id;

    @PostConstruct
    void init() {
        this.id = UUID.randomUUID().toString();
    }

    String getId() {
        return id;
    }

    @PreDestroy
    void destroy() {
        ActionSequence.addAction(id);
    }
}
