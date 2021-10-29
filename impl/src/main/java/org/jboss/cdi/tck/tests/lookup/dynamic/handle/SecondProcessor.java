package org.jboss.cdi.tck.tests.lookup.dynamic.handle;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.cdi.tck.util.ActionSequence;

@ApplicationScoped
public class SecondProcessor implements Processor {

    @Override
    public void ping() {
        ActionSequence.addAction("secondPing");
    }

    @PreDestroy
    void destroy() {
        ActionSequence.addAction("secondDestroy");
    }
}
