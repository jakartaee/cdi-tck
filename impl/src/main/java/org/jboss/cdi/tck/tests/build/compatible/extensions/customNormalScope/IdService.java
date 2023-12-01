package org.jboss.cdi.tck.tests.build.compatible.extensions.customNormalScope;

import java.util.UUID;

@CommandScoped
public class IdService {
    private final String id = UUID.randomUUID().toString();

    public String get() {
        return id;
    }
}
