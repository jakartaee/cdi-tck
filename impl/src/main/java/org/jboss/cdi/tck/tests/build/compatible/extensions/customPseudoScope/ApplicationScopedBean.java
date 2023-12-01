package org.jboss.cdi.tck.tests.build.compatible.extensions.customPseudoScope;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ApplicationScopedBean {
    @Inject
    PrototypeBean prototype;

    public String getPrototypeId() {
        return prototype.getId();
    }
}
