package org.jboss.cdi.tck.tests.build.compatible.extensions.customNormalScope;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MyService {
    @Inject
    CommandExecution execution;

    @Inject
    IdService id;

    public void process() {
        execution.getData().put("id", id.get());
    }
}
