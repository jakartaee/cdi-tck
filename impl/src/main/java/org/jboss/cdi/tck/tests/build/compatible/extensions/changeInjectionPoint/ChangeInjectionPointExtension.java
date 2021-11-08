package org.jboss.cdi.tck.tests.build.compatible.extensions.changeInjectionPoint;

import jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;

public class ChangeInjectionPointExtension implements BuildCompatibleExtension {
    @Enhancement(types = MyOtherService.class)
    public void service(ClassConfig clazz) {
        clazz.fields()
                .stream()
                .filter(it -> "myService".equals(it.info().name()))
                .forEach(field -> field.addAnnotation(AnnotationBuilder.of(MyQualifier.class).build()));
    }
}
