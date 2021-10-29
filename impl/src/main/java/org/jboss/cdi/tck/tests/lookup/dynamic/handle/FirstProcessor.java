package org.jboss.cdi.tck.tests.lookup.dynamic.handle;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;
import org.jboss.cdi.tck.util.ActionSequence;

@Dependent
public class FirstProcessor implements Processor {

    @Override
    public void ping() {
        ActionSequence.addAction("firstPing");
    }

    @PreDestroy
    void destroy() {
        ActionSequence.addAction("firstDestroy");
    }
}
