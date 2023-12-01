package org.jboss.cdi.tck.tests.build.compatible.extensions.customPseudoScope;

import java.util.UUID;

@PrototypeScoped // @Prototype itself is not a bean defining annotation, as it is a pseudo-scope
public class PrototypeBean {
    private final String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }
}
