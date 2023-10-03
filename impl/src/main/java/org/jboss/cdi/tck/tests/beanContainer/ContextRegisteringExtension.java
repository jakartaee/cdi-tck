package org.jboss.cdi.tck.tests.beanContainer;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.MetaAnnotations;

public class ContextRegisteringExtension implements BuildCompatibleExtension {

    @Discovery
    public void discovery(MetaAnnotations metaAnnotations) {
        metaAnnotations.addContext(CustomScoped.class, CustomContextImpl1.class);
        metaAnnotations.addContext(CustomScoped.class, CustomContextImpl2.class);
    }
}
