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

        assert method.receiverType() != null;
        // Following assertions yield different results with JDK 11 and JDK 17 when accessed through reflection, TCK accepts both variants
        assert method.receiverType().isClass() || method.receiverType().isParameterizedType();
        if (method.receiverType().isClass()) { // JDK 11 reflection sees this as a class
            assert method.receiverType().asClass().annotations().size() == 1;
            assert method.receiverType().asClass().hasAnnotation(AnnReceiver1.class);
            assert method.receiverType().asClass().declaration().name().equals("org.jboss.cdi.lang.model.tck.ReceiverOnGenericClass");
        } else if (method.receiverType().isParameterizedType()) { // JDK 17 reflection correctly evaluates this as a parameterized type
            assert method.receiverType().asParameterizedType().annotations().size() == 1;
            assert method.receiverType().asParameterizedType().hasAnnotation(AnnReceiver1.class);
            assert method.receiverType().asParameterizedType().declaration().name().equals("org.jboss.cdi.lang.model.tck.ReceiverOnGenericClass");
        }
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
        // Following assertions yield different results with JDK 11 and JDK 17 when accessed through reflection, TCK accepts both variants
        assert method.receiverType().isClass() || method.receiverType().isParameterizedType();
        if (method.receiverType().isClass()) { // JDK 11 reflection sees this as a class
            assert method.receiverType().asClass().annotations().isEmpty();
            assert method.receiverType().asClass().declaration().equals(clazz);
        } else if (method.receiverType().isParameterizedType()) { // JDK 17 reflection correctly evaluates this as a parameterized type
            assert method.receiverType().asParameterizedType().annotations().isEmpty();
            assert method.receiverType().asParameterizedType().declaration().equals(clazz);
        }
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
