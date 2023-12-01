package org.jboss.cdi.tck.tests.build.compatible.extensions.customPseudoScope;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class RequestScopedBean {
    @Inject
    PrototypeBean prototype;

    public String getPrototypeId() {
        return prototype.getId();
    }
}
