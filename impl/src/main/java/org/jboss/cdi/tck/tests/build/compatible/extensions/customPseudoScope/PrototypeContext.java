package org.jboss.cdi.tck.tests.build.compatible.extensions.customPseudoScope;

import jakarta.enterprise.context.spi.AlterableContext;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;

import java.lang.annotation.Annotation;

public class PrototypeContext implements AlterableContext {
    public Class<? extends Annotation> getScope() {
        return Prototype.class;
    }

    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        return creationalContext != null ? contextual.create(creationalContext) : null;
    }

    public <T> T get(Contextual<T> contextual) {
        return null;
    }

    public boolean isActive() {
        return true;
    }

    public void destroy(Contextual<?> contextual) {
    }
}
