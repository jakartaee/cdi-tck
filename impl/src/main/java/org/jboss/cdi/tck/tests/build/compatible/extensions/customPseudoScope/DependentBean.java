package org.jboss.cdi.tck.tests.build.compatible.extensions.customPseudoScope;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class DependentBean {
    @Inject
    PrototypeBean prototype;

    public String getPrototypeId() {
        return prototype.getId();
    }
}
