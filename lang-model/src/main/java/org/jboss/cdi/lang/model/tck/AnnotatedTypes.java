package org.jboss.cdi.lang.model.tck;

import jakarta.enterprise.lang.model.declarations.ClassInfo;
import jakarta.enterprise.lang.model.declarations.FieldInfo;
import jakarta.enterprise.lang.model.declarations.MethodInfo;
import jakarta.enterprise.lang.model.types.PrimitiveType;
import jakarta.enterprise.lang.model.types.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

import static org.jboss.cdi.lang.model.tck.PlainClassMembers.Verifier.assertType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface AnnClass {
}

// TYPE is probably useless here
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableClass1 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableClass2 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableClass3 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableClass4 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableClass5 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableClass6 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableClass7 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableClass8 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
@interface AnnConstructor {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE_USE})
@interface AnnConstructorParameter {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE_USE})
@interface AnnVoidMethod {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE_USE})
@interface AnnMethodParameter {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@interface AnnPrimitiveField {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@interface AnnClassField {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@interface AnnArrayField1 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@interface AnnArrayField2 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@interface AnnArrayField3 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@interface AnnArrayField4 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@interface AnnParameterizedField1 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@interface AnnParameterizedField2 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@interface AnnParameterizedField3 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@interface AnnParameterizedField4 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@interface AnnTypeVariableField {
}

// METHOD is probably useless here
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableMethod1 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableMethod2 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableMethod3 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableMethod4 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableMethod5 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableMethod6 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableMethod7 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnTypeVariableMethod8 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnWildcardMethod1 {
}

// PARAMETER is probably useless here
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnWildcardMethod2 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnWildcardMethod3 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnWildcardMethod4 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnWildcardMethod5 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnWildcardMethod6 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface AnnWildcardMethod7 {
}

@AnnClass
public class AnnotatedTypes<@AnnTypeVariableClass1 A,
        @AnnTypeVariableClass2 B extends @AnnTypeVariableClass3 A,
        @AnnTypeVariableClass4 C extends @AnnTypeVariableClass5 Number,
        @AnnTypeVariableClass6 D extends @AnnTypeVariableClass7 Number & @AnnTypeVariableClass8 CharSequence> {

    @AnnConstructor
    AnnotatedTypes(@AnnConstructorParameter String parameter) {
    }

    @AnnVoidMethod
    void voidMethod(@AnnMethodParameter String parameter) {
    }

    @AnnPrimitiveField
    int primitiveField;

    @AnnClassField
    String classField;

    @AnnArrayField4
    String[] @AnnArrayField1 [][] @AnnArrayField2 [][] @AnnArrayField3 [] arrayField;

    @AnnParameterizedField1
    List<@AnnParameterizedField2 Map<@AnnParameterizedField3 String, @AnnParameterizedField4 A>> parameterizedField;

    @AnnTypeVariableField
    B typeVariableField;

    <@AnnTypeVariableMethod1 T,
            @AnnTypeVariableMethod2 U extends @AnnTypeVariableMethod3 C,
            @AnnTypeVariableMethod4 V extends @AnnTypeVariableMethod5 Number,
            @AnnTypeVariableMethod6 W extends @AnnTypeVariableMethod7 Number & @AnnTypeVariableMethod8 CharSequence>
    void typeVariableMethod() {
    }

    void wildcardMethod(
            List<@AnnWildcardMethod1 ? extends @AnnWildcardMethod2 D> p1,
            List<@AnnWildcardMethod3 ? super @AnnWildcardMethod4 String> p2,
            List<@AnnWildcardMethod5 ?> p3,
            List<@AnnWildcardMethod6 ? extends @AnnWildcardMethod7 Object> p4) {
    }

    public static void verify(ClassInfo clazz) {
        verifyTypeParameters(clazz);
        verifyConstructor(clazz);
        verifyVoidMethod(clazz);
        verifyPrimitiveField(clazz);
        verifyClassField(clazz);
        verifyArrayField(clazz);
        verifyParameterizedField(clazz);
        verifyTypeVariableField(clazz);
        verifyTypeVariableMethod(clazz);
        verifyWildcardMethod(clazz);
    }

    private static void verifyTypeParameters(ClassInfo clazz) {
        assert clazz.typeParameters().size() == 4;

        // @AnnTypeVariableClass1 A
        assert clazz.typeParameters().get(0).isTypeVariable();
        assert clazz.typeParameters().get(0).asTypeVariable().annotations().size() == 1;
        assert clazz.typeParameters().get(0).asTypeVariable().hasAnnotation(AnnTypeVariableClass1.class);
        assert clazz.typeParameters().get(0).asTypeVariable().name().equals("A");
        assert clazz.typeParameters().get(0).asTypeVariable().bounds().size() == 1;
        assert clazz.typeParameters().get(0).asTypeVariable().bounds().get(0).isClass();
        assert clazz.typeParameters().get(0).asTypeVariable().bounds().get(0).asClass().declaration().name().equals("java.lang.Object");

        // @AnnTypeVariableClass2 B extends @AnnTypeVariableClass3 A
        assert clazz.typeParameters().get(1).isTypeVariable();
        assert clazz.typeParameters().get(1).asTypeVariable().annotations().size() == 1;
        assert clazz.typeParameters().get(1).asTypeVariable().hasAnnotation(AnnTypeVariableClass2.class);
        assert clazz.typeParameters().get(1).asTypeVariable().name().equals("B");
        assert clazz.typeParameters().get(1).asTypeVariable().bounds().size() == 1;
        assert clazz.typeParameters().get(1).asTypeVariable().bounds().get(0).isTypeVariable();
        assert clazz.typeParameters().get(1).asTypeVariable().bounds().get(0).asTypeVariable().annotations().size() == 1;
        assert clazz.typeParameters().get(1).asTypeVariable().bounds().get(0).asTypeVariable().hasAnnotation(AnnTypeVariableClass3.class);
        assert clazz.typeParameters().get(1).asTypeVariable().bounds().get(0).asTypeVariable().name().equals("A");

        // @AnnTypeVariableClass4 C extends @AnnTypeVariableClass5 Number
        assert clazz.typeParameters().get(2).isTypeVariable();
        assert clazz.typeParameters().get(2).asTypeVariable().annotations().size() == 1;
        assert clazz.typeParameters().get(2).asTypeVariable().hasAnnotation(AnnTypeVariableClass4.class);
        assert clazz.typeParameters().get(2).asTypeVariable().name().equals("C");
        assert clazz.typeParameters().get(2).asTypeVariable().bounds().size() == 1;
        assert clazz.typeParameters().get(2).asTypeVariable().bounds().get(0).isClass();
        assert clazz.typeParameters().get(2).asTypeVariable().bounds().get(0).asClass().annotations().size() == 1;
        assert clazz.typeParameters().get(2).asTypeVariable().bounds().get(0).asClass().hasAnnotation(AnnTypeVariableClass5.class);
        assert clazz.typeParameters().get(2).asTypeVariable().bounds().get(0).asClass().declaration().isPlainClass();
        assert clazz.typeParameters().get(2).asTypeVariable().bounds().get(0).asClass().declaration().name().equals("java.lang.Number");

        // @AnnTypeVariableClass6 D extends @AnnTypeVariableClass7 Number & @AnnTypeVariableClass8 CharSequence
        assert clazz.typeParameters().get(3).isTypeVariable();
        assert clazz.typeParameters().get(3).asTypeVariable().annotations().size() == 1;
        assert clazz.typeParameters().get(3).asTypeVariable().hasAnnotation(AnnTypeVariableClass6.class);
        assert clazz.typeParameters().get(3).asTypeVariable().name().equals("D");
        assert clazz.typeParameters().get(3).asTypeVariable().bounds().size() == 2;
        assert clazz.typeParameters().get(3).asTypeVariable().bounds().get(0).isClass();
        assert clazz.typeParameters().get(3).asTypeVariable().bounds().get(0).asClass().annotations().size() == 1;
        assert clazz.typeParameters().get(3).asTypeVariable().bounds().get(0).asClass().hasAnnotation(AnnTypeVariableClass7.class);
        assert clazz.typeParameters().get(3).asTypeVariable().bounds().get(0).asClass().declaration().name().equals("java.lang.Number");
        assert clazz.typeParameters().get(3).asTypeVariable().bounds().get(1).isClass();
        assert clazz.typeParameters().get(3).asTypeVariable().bounds().get(1).asClass().annotations().size() == 1;
        assert clazz.typeParameters().get(3).asTypeVariable().bounds().get(1).asClass().hasAnnotation(AnnTypeVariableClass8.class);
        assert clazz.typeParameters().get(3).asTypeVariable().bounds().get(1).asClass().declaration().isInterface();
        assert clazz.typeParameters().get(3).asTypeVariable().bounds().get(1).asClass().declaration().name().equals("java.lang.CharSequence");

        assertType(clazz.typeParameters().get(0), Type.Kind.TYPE_VARIABLE);
        assertType(clazz.typeParameters().get(1), Type.Kind.TYPE_VARIABLE);
        assertType(clazz.typeParameters().get(2), Type.Kind.TYPE_VARIABLE);
        assertType(clazz.typeParameters().get(3), Type.Kind.TYPE_VARIABLE);
    }

    private static void verifyConstructor(ClassInfo clazz) {
        assert clazz.constructors().size() == 1;
        MethodInfo ctor = clazz.constructors().iterator().next();

        // @AnnConstructor
        // AnnotatedTypes(@AnnConstructorParameter String parameter)
        assert ctor.annotations().size() == 1;
        assert ctor.hasAnnotation(AnnConstructor.class);
        // TODO need to check!
        //assert ctor.returnType().annotations().size() == 1;
        //assert ctor.returnType().hasAnnotation(AnnConstructor.class);
        assert ctor.parameters().size() == 1;
        assert ctor.parameters().get(0).annotations().size() == 1;
        assert ctor.parameters().get(0).hasAnnotation(AnnConstructorParameter.class);
        assert ctor.parameters().get(0).type().isClass();
        assert ctor.parameters().get(0).type().asClass().annotations().size() == 1;
        assert ctor.parameters().get(0).type().asClass().hasAnnotation(AnnConstructorParameter.class);
        assert ctor.parameters().get(0).type().asClass().declaration().name().equals("java.lang.String");
    }

    private static void verifyVoidMethod(ClassInfo clazz) {
        MethodInfo method = LangModelUtils.singleMethod(clazz, "voidMethod");

        // @AnnVoidMethod
        // void voidMethod(@AnnMethodParameter String parameter)
        assert method.annotations().size() == 1;
        assert method.hasAnnotation(AnnVoidMethod.class);

        assert method.returnType().isVoid();
        // TODO need to check!
        //assert method.returnType().annotations().size() == 1;
        //assert method.returnType().hasAnnotation(AnnVoidMethod.class);

        assert method.parameters().size() == 1;
        assert method.parameters().get(0).annotations().size() == 1;
        assert method.parameters().get(0).hasAnnotation(AnnMethodParameter.class);
        assert method.parameters().get(0).type().isClass();
        assert method.parameters().get(0).type().asClass().annotations().size() == 1;
        assert method.parameters().get(0).type().asClass().hasAnnotation(AnnMethodParameter.class);
        assert method.parameters().get(0).type().asClass().declaration().name().equals("java.lang.String");

        assertType(method.returnType(), Type.Kind.VOID);
    }

    private static void verifyPrimitiveField(ClassInfo clazz) {
        FieldInfo field = LangModelUtils.singleField(clazz, "primitiveField");

        // @AnnPrimitiveField
        // int primitiveField
        assert field.annotations().size() == 1;
        assert field.hasAnnotation(AnnPrimitiveField.class);

        assert field.type().isPrimitive();
        assert field.type().asPrimitive().annotations().size() == 1;
        assert field.type().asPrimitive().hasAnnotation(AnnPrimitiveField.class);
        assert field.type().asPrimitive().primitiveKind() == PrimitiveType.PrimitiveKind.INT;

        assertType(field.type(), Type.Kind.PRIMITIVE);
    }

    private static void verifyClassField(ClassInfo clazz) {
        FieldInfo field = LangModelUtils.singleField(clazz, "classField");

        // @AnnClassField
        // String classField
        assert field.annotations().size() == 1;
        assert field.hasAnnotation(AnnClassField.class);

        assert field.type().isClass();
        assert field.type().asClass().annotations().size() == 1;
        assert field.type().asClass().hasAnnotation(AnnClassField.class);
        assert field.type().asClass().declaration().name().equals("java.lang.String");

        assertType(field.type(), Type.Kind.CLASS);
    }

    private static void verifyArrayField(ClassInfo clazz) {
        FieldInfo field = LangModelUtils.singleField(clazz, "arrayField");

        // @AnnArrayField4
        // String[] @AnnArrayField1 [][] @AnnArrayField2 [][] @AnnArrayField3 [] arrayField
        assert field.annotations().size() == 1;
        assert field.hasAnnotation(AnnArrayField4.class);

        // @AnnArrayField4 String [] @AnnArrayField1 [][] @AnnArrayField2 [][] @AnnArrayField3 []
        Type type = field.type();
        assert type.isArray();
        assert type.asArray().annotations().isEmpty();

        // @AnnArrayField4 String @AnnArrayField1 [][] @AnnArrayField2 [][] @AnnArrayField3 []
        type = type.asArray().componentType();
        assert type.isArray();
        assert type.asArray().annotations().size() == 1;
        assert type.asArray().hasAnnotation(AnnArrayField1.class);

        // @AnnArrayField4 String [] @AnnArrayField2 [][] @AnnArrayField3 []
        type = type.asArray().componentType();
        assert type.isArray();
        assert type.asArray().annotations().isEmpty();

        // @AnnArrayField4 String @AnnArrayField2 [][] @AnnArrayField3 []
        type = type.asArray().componentType();
        assert type.isArray();
        assert type.asArray().annotations().size() == 1;
        assert type.asArray().hasAnnotation(AnnArrayField2.class);

        // @AnnArrayField4 String [] @AnnArrayField3 []
        type = type.asArray().componentType();
        assert type.isArray();
        assert type.asArray().annotations().isEmpty();

        // @AnnArrayField4 String @AnnArrayField3 []
        type = type.asArray().componentType();
        assert type.isArray();
        assert type.asArray().annotations().size() == 1;
        assert type.asArray().hasAnnotation(AnnArrayField3.class);

        // @AnnArrayField4 String
        type = type.asArray().componentType();
        assert type.isClass();
        assert type.asClass().annotations().size() == 1;
        assert type.asClass().hasAnnotation(AnnArrayField4.class);
        assert type.asClass().declaration().name().equals("java.lang.String");

        assertType(field.type(), Type.Kind.ARRAY);
    }

    private static void verifyParameterizedField(ClassInfo clazz) {
        FieldInfo field = LangModelUtils.singleField(clazz, "parameterizedField");

        // @AnnParameterizedField1
        // List<@AnnParameterizedField2 Map<@AnnParameterizedField3 String, @AnnParameterizedField4 A>> parameterizedField
        assert field.annotations().size() == 1;
        assert field.hasAnnotation(AnnParameterizedField1.class);

        // @AnnParameterizedField1 List<@AnnParameterizedField2 Map<@AnnParameterizedField3 String, @AnnParameterizedField4 A>>
        assert field.type().isParameterizedType();
        assert field.type().asParameterizedType().annotations().size() == 1;
        assert field.type().asParameterizedType().hasAnnotation(AnnParameterizedField1.class);
        assert field.type().asParameterizedType().declaration().name().equals("java.util.List");
        assert field.type().asParameterizedType().typeArguments().size() == 1;

        Type typearg1 = field.type().asParameterizedType().typeArguments().get(0);

        // @AnnParameterizedField2 Map<@AnnParameterizedField3 String, @AnnParameterizedField4 A>
        assert typearg1.isParameterizedType();
        assert typearg1.asParameterizedType().annotations().size() == 1;
        assert typearg1.asParameterizedType().hasAnnotation(AnnParameterizedField2.class);
        assert typearg1.asParameterizedType().declaration().name().equals("java.util.Map");
        assert typearg1.asParameterizedType().typeArguments().size() == 2;

        Type typearg2 = typearg1.asParameterizedType().typeArguments().get(0);
        Type typearg3 = typearg1.asParameterizedType().typeArguments().get(1);

        // @AnnParameterizedField3 String
        assert typearg2.isClass();
        assert typearg2.asClass().annotations().size() == 1;
        assert typearg2.asClass().hasAnnotation(AnnParameterizedField3.class);
        assert typearg2.asClass().declaration().name().equals("java.lang.String");

        // @AnnParameterizedField4 A
        assert typearg3.isTypeVariable();
        assert typearg3.asTypeVariable().annotations().size() == 1;
        assert typearg3.asTypeVariable().hasAnnotation(AnnParameterizedField4.class);
        assert typearg3.asTypeVariable().name().equals("A");

        assertType(field.type(), Type.Kind.PARAMETERIZED_TYPE);
    }

    private static void verifyTypeVariableField(ClassInfo clazz) {
        FieldInfo field = LangModelUtils.singleField(clazz, "typeVariableField");

        // @AnnTypeVariableField
        // B typeVariableField
        assert field.annotations().size() == 1;
        assert field.hasAnnotation(AnnTypeVariableField.class);

        assert field.type().isTypeVariable();
        assert field.type().asTypeVariable().annotations().size() == 1;
        assert field.type().asTypeVariable().hasAnnotation(AnnTypeVariableField.class);
        assert field.type().asTypeVariable().name().equals("B");
        // TODO need to check!
        assert field.type().asTypeVariable().bounds().size() == 1;
        assert field.type().asTypeVariable().bounds().get(0).isTypeVariable();
        assert field.type().asTypeVariable().bounds().get(0).asTypeVariable().name().equals("A");

        assertType(field.type(), Type.Kind.TYPE_VARIABLE);
    }

    private static void verifyTypeVariableMethod(ClassInfo clazz) {
        MethodInfo method = LangModelUtils.singleMethod(clazz, "typeVariableMethod");

        // <@AnnTypeVariableMethod1 T,
        //     @AnnTypeVariableMethod2 U extends @AnnTypeVariableMethod3 C,
        //     @AnnTypeVariableMethod4 V extends @AnnTypeVariableMethod5 Number,
        //     @AnnTypeVariableMethod6 W extends @AnnTypeVariableMethod7 Number & @AnnTypeVariableMethod8 CharSequence>
        // void typeVariableMethod()
        assert method.typeParameters().size() == 4;

        // @AnnTypeVariableMethod1 T
        assert method.typeParameters().get(0).isTypeVariable();
        assert method.typeParameters().get(0).asTypeVariable().annotations().size() == 1;
        assert method.typeParameters().get(0).asTypeVariable().hasAnnotation(AnnTypeVariableMethod1.class);
        assert method.typeParameters().get(0).asTypeVariable().name().equals("T");
        assert method.typeParameters().get(0).asTypeVariable().bounds().size() == 1;
        assert method.typeParameters().get(0).asTypeVariable().bounds().get(0).isClass();
        assert method.typeParameters().get(0).asTypeVariable().bounds().get(0).asClass().declaration().name().equals("java.lang.Object");

        // @AnnTypeVariableMethod2 U extends @AnnTypeVariableMethod3 C
        assert method.typeParameters().get(1).isTypeVariable();
        assert method.typeParameters().get(1).asTypeVariable().annotations().size() == 1;
        assert method.typeParameters().get(1).asTypeVariable().hasAnnotation(AnnTypeVariableMethod2.class);
        assert method.typeParameters().get(1).asTypeVariable().name().equals("U");
        assert method.typeParameters().get(1).asTypeVariable().bounds().size() == 1;
        assert method.typeParameters().get(1).asTypeVariable().bounds().get(0).isTypeVariable();
        assert method.typeParameters().get(1).asTypeVariable().bounds().get(0).asTypeVariable().annotations().size() == 1;
        assert method.typeParameters().get(1).asTypeVariable().bounds().get(0).asTypeVariable().hasAnnotation(AnnTypeVariableMethod3.class);
        assert method.typeParameters().get(1).asTypeVariable().bounds().get(0).asTypeVariable().name().equals("C");
        // TODO need to check!
        assert method.typeParameters().get(1).asTypeVariable().bounds().get(0).asTypeVariable().bounds().size() == 1;
        assert method.typeParameters().get(1).asTypeVariable().bounds().get(0).asTypeVariable().bounds().get(0).isClass();
        assert method.typeParameters().get(1).asTypeVariable().bounds().get(0).asTypeVariable().bounds().get(0).asClass().declaration().name().equals("java.lang.Number");

        // @AnnTypeVariableMethod4 V extends @AnnTypeVariableMethod5 Number
        assert method.typeParameters().get(2).isTypeVariable();
        assert method.typeParameters().get(2).asTypeVariable().annotations().size() == 1;
        assert method.typeParameters().get(2).asTypeVariable().hasAnnotation(AnnTypeVariableMethod4.class);
        assert method.typeParameters().get(2).asTypeVariable().name().equals("V");
        assert method.typeParameters().get(2).asTypeVariable().bounds().size() == 1;
        assert method.typeParameters().get(2).asTypeVariable().bounds().get(0).isClass();
        assert method.typeParameters().get(2).asTypeVariable().bounds().get(0).asClass().annotations().size() == 1;
        assert method.typeParameters().get(2).asTypeVariable().bounds().get(0).asClass().hasAnnotation(AnnTypeVariableMethod5.class);
        assert method.typeParameters().get(2).asTypeVariable().bounds().get(0).asClass().declaration().name().equals("java.lang.Number");

        // @AnnTypeVariableMethod6 W extends @AnnTypeVariableMethod7 Number & @AnnTypeVariableMethod8 CharSequence
        assert method.typeParameters().get(3).isTypeVariable();
        assert method.typeParameters().get(3).asTypeVariable().annotations().size() == 1;
        assert method.typeParameters().get(3).asTypeVariable().hasAnnotation(AnnTypeVariableMethod6.class);
        assert method.typeParameters().get(3).asTypeVariable().name().equals("W");
        assert method.typeParameters().get(3).asTypeVariable().bounds().size() == 2;
        assert method.typeParameters().get(3).asTypeVariable().bounds().get(0).isClass();
        assert method.typeParameters().get(3).asTypeVariable().bounds().get(0).asClass().annotations().size() == 1;
        assert method.typeParameters().get(3).asTypeVariable().bounds().get(0).asClass().hasAnnotation(AnnTypeVariableMethod7.class);
        assert method.typeParameters().get(3).asTypeVariable().bounds().get(0).asClass().declaration().name().equals("java.lang.Number");
        assert method.typeParameters().get(3).asTypeVariable().bounds().get(1).isClass();
        assert method.typeParameters().get(3).asTypeVariable().bounds().get(1).asClass().annotations().size() == 1;
        assert method.typeParameters().get(3).asTypeVariable().bounds().get(1).asClass().hasAnnotation(AnnTypeVariableMethod8.class);
        assert method.typeParameters().get(3).asTypeVariable().bounds().get(1).asClass().declaration().name().equals("java.lang.CharSequence");

        assertType(method.typeParameters().get(0), Type.Kind.TYPE_VARIABLE);
        assertType(method.typeParameters().get(1), Type.Kind.TYPE_VARIABLE);
        assertType(method.typeParameters().get(2), Type.Kind.TYPE_VARIABLE);
        assertType(method.typeParameters().get(3), Type.Kind.TYPE_VARIABLE);
    }

    private static void verifyWildcardMethod(ClassInfo clazz) {
        MethodInfo method = LangModelUtils.singleMethod(clazz, "wildcardMethod");

        // void wildcardMethod(
        //     List<@AnnWildcardMethod1 ? extends @AnnWildcardMethod2 D> p1,
        //     List<@AnnWildcardMethod3 ? super @AnnWildcardMethod4 String> p2,
        //     List<@AnnWildcardMethod5 ?> p3,
        //     List<@AnnWildcardMethod6 ? extends @AnnWildcardMethod7 Object> p4)
        assert method.parameters().size() == 4;

        // List<@AnnWildcardMethod1 ? extends @AnnWildcardMethod2 D>
        Type param1 = method.parameters().get(0).type();
        assert param1.isParameterizedType();
        assert param1.asParameterizedType().typeArguments().size() == 1;
        assert param1.asParameterizedType().typeArguments().get(0).isWildcardType();
        assert param1.asParameterizedType().typeArguments().get(0).asWildcardType().annotations().size() == 1;
        assert param1.asParameterizedType().typeArguments().get(0).asWildcardType().hasAnnotation(AnnWildcardMethod1.class);
        assert param1.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound() != null;
        assert param1.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound().isTypeVariable();
        assert param1.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound().asTypeVariable().annotations().size() == 1;
        assert param1.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound().asTypeVariable().hasAnnotation(AnnWildcardMethod2.class);
        assert param1.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound().asTypeVariable().name().equals("D");
        assert param1.asParameterizedType().typeArguments().get(0).asWildcardType().lowerBound() == null;

        // List<@AnnWildcardMethod3 ? super @AnnWildcardMethod4 String>
        Type param2 = method.parameters().get(1).type();
        assert param2.isParameterizedType();
        assert param2.asParameterizedType().typeArguments().size() == 1;
        assert param2.asParameterizedType().typeArguments().get(0).isWildcardType();
        assert param2.asParameterizedType().typeArguments().get(0).asWildcardType().annotations().size() == 1;
        assert param2.asParameterizedType().typeArguments().get(0).asWildcardType().hasAnnotation(AnnWildcardMethod3.class);
        assert param2.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound() == null;
        assert param2.asParameterizedType().typeArguments().get(0).asWildcardType().lowerBound() != null;
        assert param2.asParameterizedType().typeArguments().get(0).asWildcardType().lowerBound().isClass();
        assert param2.asParameterizedType().typeArguments().get(0).asWildcardType().lowerBound().asClass().annotations().size() == 1;
        assert param2.asParameterizedType().typeArguments().get(0).asWildcardType().lowerBound().asClass().hasAnnotation(AnnWildcardMethod4.class);
        assert param2.asParameterizedType().typeArguments().get(0).asWildcardType().lowerBound().asClass().declaration().name().equals("java.lang.String");

        // List<@AnnWildcardMethod5 ?>
        Type param3 = method.parameters().get(2).type();
        assert param3.isParameterizedType();
        assert param3.asParameterizedType().typeArguments().size() == 1;
        assert param3.asParameterizedType().typeArguments().get(0).isWildcardType();
        assert param3.asParameterizedType().typeArguments().get(0).asWildcardType().annotations().size() == 1;
        assert param3.asParameterizedType().typeArguments().get(0).asWildcardType().hasAnnotation(AnnWildcardMethod5.class);
        // TODO this currently does NOT reflect the spec, but shows that perhaps the spec needs to be changed!
        //assert param3.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound() == null;
        assert param3.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound() != null;
        assert param3.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound().isClass();
        assert param3.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound().asClass().annotations().isEmpty();
        assert param3.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound().asClass().declaration().name().equals("java.lang.Object");
        assert param3.asParameterizedType().typeArguments().get(0).asWildcardType().lowerBound() == null;

        // List<@AnnWildcardMethod6 ? extends @AnnWildcardMethod7 Object> p4
        // TODO this currently does NOT reflect the spec, but shows that perhaps the spec needs to be changed!
        Type param4 = method.parameters().get(3).type();
        assert param4.isParameterizedType();
        assert param4.asParameterizedType().typeArguments().size() == 1;
        assert param4.asParameterizedType().typeArguments().get(0).isWildcardType();
        assert param4.asParameterizedType().typeArguments().get(0).asWildcardType().annotations().size() == 1;
        assert param4.asParameterizedType().typeArguments().get(0).asWildcardType().hasAnnotation(AnnWildcardMethod6.class);
        assert param4.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound() != null;
        assert param4.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound().isClass();
        assert param4.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound().asClass().annotations().size() == 1;
        assert param4.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound().asClass().hasAnnotation(AnnWildcardMethod7.class);
        assert param4.asParameterizedType().typeArguments().get(0).asWildcardType().upperBound().asClass().declaration().name().equals("java.lang.Object");
        assert param4.asParameterizedType().typeArguments().get(0).asWildcardType().lowerBound() == null;

        assertType(method.parameters().get(0).type().asParameterizedType().typeArguments().get(0), Type.Kind.WILDCARD_TYPE);
        assertType(method.parameters().get(1).type().asParameterizedType().typeArguments().get(0), Type.Kind.WILDCARD_TYPE);
        assertType(method.parameters().get(2).type().asParameterizedType().typeArguments().get(0), Type.Kind.WILDCARD_TYPE);
        assertType(method.parameters().get(3).type().asParameterizedType().typeArguments().get(0), Type.Kind.WILDCARD_TYPE);
    }
}
