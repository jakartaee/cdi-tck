package org.jboss.cdi.lang.model.tck;

import jakarta.enterprise.lang.model.declarations.ClassInfo;
import jakarta.enterprise.lang.model.declarations.MethodInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnReceiver1 {
}

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnReceiver2 {
}

class ReceiverOnGenericClass<T> {
    public ReceiverOnGenericClass() {
    }

    public static void staticMethod() {
    }

    public void methodWithoutReceiver() {
    }

    public void methodWithReceiver(@AnnReceiver1 ReceiverOnGenericClass<T> this) {
    }
}

class ReceiverOnClass {
    public ReceiverOnClass() {
    }

    public static void staticMethod() {
    }

    public void methodWithoutReceiver() {
    }

    public void methodWithReceiver(@AnnReceiver2 ReceiverOnClass this) {
    }
}

public class AnnotatedReceiverTypes<T> {
    ReceiverOnGenericClass receiverOnGenericClass;
    ReceiverOnClass receiverOnClass;

    public static void verify(ClassInfo clazz) {
        ClassInfo receiverOnGenericClass = LangModelUtils.classOfField(clazz, "receiverOnGenericClass");

        verifyMethodWithReceiverOnGenericClass(receiverOnGenericClass);
        verifyMethodWithoutReceiver(receiverOnGenericClass);
        verifyStaticMethod(receiverOnGenericClass);
        verifyConstructor(receiverOnGenericClass);

        ClassInfo receiverOnClass = LangModelUtils.classOfField(clazz, "receiverOnClass");
        verifyMethodWithReceiverOnClass(receiverOnClass);
        verifyMethodWithoutReceiver(receiverOnClass);
        verifyStaticMethod(receiverOnClass);
        verifyConstructor(receiverOnClass);
    }

    private static void verifyMethodWithReceiverOnGenericClass(ClassInfo clazz) {
        MethodInfo method = LangModelUtils.singleMethod(clazz, "methodWithReceiver");

        // reflection returns `AnnotatedType` which is not `AnnotatedParameterizedType`,
        // so here, we expect the same -- `ClassType`, not `ParameterizedType`
        assert method.receiverType() != null;
        assert method.receiverType().isClass();
        assert method.receiverType().asClass().annotations().size() == 1;
        assert method.receiverType().asClass().hasAnnotation(AnnReceiver1.class);
        assert method.receiverType().asClass().declaration().name().equals("org.jboss.cdi.lang.model.tck.ReceiverOnGenericClass");
    }

    private static void verifyMethodWithReceiverOnClass(ClassInfo clazz) {
        MethodInfo method = LangModelUtils.singleMethod(clazz, "methodWithReceiver");

        assert method.receiverType() != null;
        assert method.receiverType().isClass();
        assert method.receiverType().asClass().annotations().size() == 1;
        assert method.receiverType().asClass().hasAnnotation(AnnReceiver2.class);
        assert method.receiverType().asClass().declaration().name().equals("org.jboss.cdi.lang.model.tck.ReceiverOnClass");
    }

    private static void verifyMethodWithoutReceiver(ClassInfo clazz) {
        MethodInfo method = LangModelUtils.singleMethod(clazz, "methodWithoutReceiver");

        assert method.receiverType() != null;
        assert method.receiverType().isClass();
        assert method.receiverType().asClass().annotations().isEmpty();
        assert method.receiverType().asClass().declaration().equals(clazz);
    }

    private static void verifyStaticMethod(ClassInfo clazz) {
        MethodInfo method = LangModelUtils.singleMethod(clazz, "staticMethod");

        assert method.receiverType() == null;
    }

    private static void verifyConstructor(ClassInfo clazz) {
        assert clazz.constructors().size() == 1;

        MethodInfo method = clazz.constructors().iterator().next();

        assert method.receiverType() == null;
    }
}
