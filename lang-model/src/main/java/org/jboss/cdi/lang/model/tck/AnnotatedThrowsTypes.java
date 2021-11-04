package org.jboss.cdi.lang.model.tck;

import jakarta.enterprise.lang.model.declarations.ClassInfo;
import jakarta.enterprise.lang.model.declarations.MethodInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnThrows1 {
}

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnThrows2 {
}

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnThrows3 {
}

public class AnnotatedThrowsTypes<E extends Exception> {
    public <F extends Throwable> void throwingMethod() throws @AnnThrows1 Exception, @AnnThrows2 E, @AnnThrows3 F {
    }

    public static void verify(ClassInfo clazz) {
        MethodInfo method = LangModelUtils.singleMethod(clazz, "throwingMethod");

        assert method.throwsTypes().size() == 3;

        // @AnnThrows1 Exception
        assert method.throwsTypes().get(0).isClass();
        assert method.throwsTypes().get(0).asClass().annotations().size() == 1;
        assert method.throwsTypes().get(0).asClass().hasAnnotation(AnnThrows1.class);
        assert method.throwsTypes().get(0).asClass().declaration().name().equals("java.lang.Exception");

        // @AnnThrows2 E
        assert method.throwsTypes().get(1).isTypeVariable();
        assert method.throwsTypes().get(1).asTypeVariable().annotations().size() == 1;
        assert method.throwsTypes().get(1).asTypeVariable().hasAnnotation(AnnThrows2.class);
        assert method.throwsTypes().get(1).asTypeVariable().name().equals("E");

        // @AnnThrows3 F
        assert method.throwsTypes().get(2).isTypeVariable();
        assert method.throwsTypes().get(2).asTypeVariable().annotations().size() == 1;
        assert method.throwsTypes().get(2).asTypeVariable().hasAnnotation(AnnThrows3.class);
        assert method.throwsTypes().get(2).asTypeVariable().name().equals("F");
    }
}
