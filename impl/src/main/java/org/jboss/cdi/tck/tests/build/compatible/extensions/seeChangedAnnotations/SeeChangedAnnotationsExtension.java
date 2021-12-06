package org.jboss.cdi.tck.tests.build.compatible.extensions.seeChangedAnnotations;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder;
import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.Messages;
import jakarta.enterprise.inject.build.compatible.spi.MethodConfig;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.build.compatible.spi.Types;
import jakarta.enterprise.inject.build.compatible.spi.Validation;
import jakarta.enterprise.lang.model.declarations.ClassInfo;
import jakarta.enterprise.lang.model.declarations.MethodInfo;
import jakarta.enterprise.lang.model.types.Type;

public class SeeChangedAnnotationsExtension implements BuildCompatibleExtension {
    @Enhancement(types = MyService.class)
    public void enhancement(ClassConfig clazz) {
        clazz.addAnnotation(AnnotationBuilder.of(MyAnnotation.class).value(clazz.info().simpleName()).build());
        for (MethodConfig method : clazz.methods()) {
            method.addAnnotation(AnnotationBuilder.of(MyAnnotation.class).value(method.info().name()).build());
        }
    }

    @Enhancement(types = MyService.class)
    @Priority(10_000)
    public void postEnhancement(ClassInfo clazz, Messages msg) {
        verifyMyServiceClass(clazz, msg);
    }

    @Registration(types = MyService.class)
    public void registration(BeanInfo bean, Messages msg) {
        verifyMyServiceClass(bean.declaringClass(), msg);
    }

    @Validation
    public void validation(Messages msg, Types types) {
        Type type = types.of(MyService.class);
        if (type == null || !type.isClass()) {
            msg.error("MyService must be a discovered class");
            return;
        }

        ClassInfo clazz = type.asClass().declaration();

        verifyMyServiceClass(clazz, msg);
    }

    private void verifyMyServiceClass(ClassInfo clazz, Messages msg) {
        if (clazz == null) {
            msg.error("MyService declaration must be available");
            return;
        }

        if (!clazz.hasAnnotation(MyAnnotation.class)) {
            msg.error("MyService must have MyAnnotation");
            return;
        }
        if (!"MyService".equals(clazz.annotation(MyAnnotation.class).value().asString())) {
            msg.error("MyAnnotation.value must be 'MyService'");
            return;
        }

        for (MethodInfo method : clazz.methods()) {
            if (!method.hasAnnotation(MyAnnotation.class)) {
                msg.error("MyService method " + method.name() + " must have MyAnnotation");
                return;
            }
            if (!method.name().equals(method.annotation(MyAnnotation.class).value().asString())) {
                msg.error("MyAnnotation.value on method must be '" + method.name() + "'");
                return;
            }
        }
    }
}
