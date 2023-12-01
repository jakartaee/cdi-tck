package org.jboss.cdi.tck.tests.build.compatible.extensions.customPseudoScope;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.MetaAnnotations;

public class CustomPseudoScopeExtension implements BuildCompatibleExtension {
    @Discovery
    public void discovery(MetaAnnotations meta) {
        meta.addContext(Prototype.class, PrototypeContext.class);
    }
}
