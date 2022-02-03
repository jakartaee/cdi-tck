package org.jboss.cdi.tck.tests.build.compatible.extensions.customInterceptorBinding;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.MetaAnnotations;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;
import jakarta.enterprise.util.Nonbinding;

public class CustomInterceptorBindingExtension implements BuildCompatibleExtension {
    @Discovery
    public void discovery(MetaAnnotations meta) {
        ClassConfig cfg = meta.addInterceptorBinding(MyCustomInterceptorBinding.class);
        cfg.methods()
                .stream()
                .filter(it -> "value".equals(it.info().name()))
                .forEach(it -> it.addAnnotation(Nonbinding.class));
    }

    @Enhancement(types = MyCustomInterceptor.class)
    public void interceptorPriority(ClassConfig clazz) {
        clazz.addAnnotation(AnnotationBuilder.of(Priority.class).value(1).build());
    }
}
