package org.jboss.cdi.tck.tests.build.compatible.extensions.customQualifier;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.MetaAnnotations;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;
import jakarta.enterprise.util.Nonbinding;

public class CustomQualifierExtension implements BuildCompatibleExtension {
    @Discovery
    public void discovery(MetaAnnotations meta, ScannedClasses scan) {
        scan.add(MyServiceFoo.class.getName());

        ClassConfig cfg = meta.addQualifier(MyCustomQualifier.class);
        cfg.methods()
                .stream()
                .filter(it -> "value".equals(it.info().name()))
                .forEach(it -> it.addAnnotation(Nonbinding.class));
    }
}
