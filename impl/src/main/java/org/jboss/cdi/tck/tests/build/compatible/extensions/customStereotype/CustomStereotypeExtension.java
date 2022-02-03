package org.jboss.cdi.tck.tests.build.compatible.extensions.customStereotype;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.MetaAnnotations;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;

public class CustomStereotypeExtension implements BuildCompatibleExtension {
    @Discovery
    public void discovery(MetaAnnotations meta, ScannedClasses scan) {
        scan.add(MyService.class.getName());

        ClassConfig cfg = meta.addStereotype(MyCustomStereotype.class);
        cfg.addAnnotation(ApplicationScoped.class);
    }
}
