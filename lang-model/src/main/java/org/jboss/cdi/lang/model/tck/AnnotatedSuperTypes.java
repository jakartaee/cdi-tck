package org.jboss.cdi.lang.model.tck;

import jakarta.enterprise.lang.model.declarations.ClassInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnSuperClass {
}

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnSuperClassTypeParam {
}

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnSuperInterface1 {
}

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnSuperInterface2 {
}

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnSuperInterfaceTypeParam1 {
}

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnSuperInterfaceTypeParam2 {
}

class AnnotatedSuperClass<T> {
}

interface AnnotatedSuperInterface1<T> {
}

interface AnnotatedSuperInterface2 {
}

public class AnnotatedSuperTypes<T>
        extends @AnnSuperClass AnnotatedSuperClass<@AnnSuperClassTypeParam T>
        implements @AnnSuperInterface1 AnnotatedSuperInterface1<@AnnSuperInterfaceTypeParam1 List<@AnnSuperInterfaceTypeParam2 String>>,
                   @AnnSuperInterface2 AnnotatedSuperInterface2 {

    public static void verify(ClassInfo clazz) {
        assert clazz.typeParameters().size() == 1;
        assert clazz.typeParameters().get(0).isTypeVariable();
        assert clazz.typeParameters().get(0).asTypeVariable().name().equals("T");

        verifyExtends(clazz);
        verifyImplements(clazz);
    }

    private static void verifyExtends(ClassInfo clazz) {
        assert clazz.superClassDeclaration().name().equals("org.jboss.cdi.lang.model.tck.AnnotatedSuperClass");

        // @AnnSuperClass AnnotatedSuperClass<@AnnSuperClassTypeParam T>
        assert clazz.superClass().isParameterizedType();
        assert clazz.superClass().asParameterizedType().annotations().size() == 1;
        assert clazz.superClass().asParameterizedType().hasAnnotation(AnnSuperClass.class);
        assert clazz.superClass().asParameterizedType().declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnotatedSuperClass");
        assert clazz.superClass().asParameterizedType().typeArguments().size() == 1;
        assert clazz.superClass().asParameterizedType().typeArguments().get(0).isTypeVariable();
        assert clazz.superClass().asParameterizedType().typeArguments().get(0).asTypeVariable().annotations().size() == 1;
        assert clazz.superClass().asParameterizedType().typeArguments().get(0).asTypeVariable().hasAnnotation(AnnSuperClassTypeParam.class);
        assert clazz.superClass().asParameterizedType().typeArguments().get(0).asTypeVariable().name().equals("T");
    }

    private static void verifyImplements(ClassInfo clazz) {
        assert clazz.superInterfacesDeclarations().size() == 2;
        assert clazz.superInterfacesDeclarations().get(0).name().equals("org.jboss.cdi.lang.model.tck.AnnotatedSuperInterface1");
        assert clazz.superInterfacesDeclarations().get(1).name().equals("org.jboss.cdi.lang.model.tck.AnnotatedSuperInterface2");

        // @AnnSuperInterface1 AnnotatedSuperInterface1<@AnnSuperInterfaceTypeParam1 List<@AnnSuperInterfaceTypeParam2 String>>,
        assert clazz.superInterfaces().get(0).isParameterizedType();
        assert clazz.superInterfaces().get(0).asParameterizedType().annotations().size() == 1;
        assert clazz.superInterfaces().get(0).asParameterizedType().hasAnnotation(AnnSuperInterface1.class);
        assert clazz.superInterfaces().get(0).asParameterizedType().declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnotatedSuperInterface1");
        assert clazz.superInterfaces().get(0).asParameterizedType().typeArguments().size() == 1;
        assert clazz.superInterfaces().get(0).asParameterizedType().typeArguments().get(0).isParameterizedType();
        assert clazz.superInterfaces().get(0).asParameterizedType().typeArguments().get(0).asParameterizedType().annotations().size() == 1;
        assert clazz.superInterfaces().get(0).asParameterizedType().typeArguments().get(0).asParameterizedType().hasAnnotation(AnnSuperInterfaceTypeParam1.class);
        assert clazz.superInterfaces().get(0).asParameterizedType().typeArguments().get(0).asParameterizedType().declaration().name().equals("java.util.List");
        assert clazz.superInterfaces().get(0).asParameterizedType().typeArguments().get(0).asParameterizedType().typeArguments().size() == 1;
        assert clazz.superInterfaces().get(0).asParameterizedType().typeArguments().get(0).asParameterizedType().typeArguments().get(0).isClass();
        assert clazz.superInterfaces().get(0).asParameterizedType().typeArguments().get(0).asParameterizedType().typeArguments().get(0).asClass().annotations().size() == 1;
        assert clazz.superInterfaces().get(0).asParameterizedType().typeArguments().get(0).asParameterizedType().typeArguments().get(0).asClass().hasAnnotation(AnnSuperInterfaceTypeParam2.class);
        assert clazz.superInterfaces().get(0).asParameterizedType().typeArguments().get(0).asParameterizedType().typeArguments().get(0).asClass().declaration().name().equals("java.lang.String");

        // @AnnSuperInterface2 AnnotatedSuperInterface2
        assert clazz.superInterfaces().get(1).isClass();
        assert clazz.superInterfaces().get(1).asClass().annotations().size() == 1;
        assert clazz.superInterfaces().get(1).asClass().hasAnnotation(AnnSuperInterface2.class);
        assert clazz.superInterfaces().get(1).asClass().declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnotatedSuperInterface2");
    }
}
