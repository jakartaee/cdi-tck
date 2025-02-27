/*
 * Copyright 2025, Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.lang.model.tck;

import static org.jboss.cdi.lang.model.tck.PlainClassMembers.Verifier.assertConstructor;
import static org.jboss.cdi.lang.model.tck.PlainClassMembers.Verifier.assertField;
import static org.jboss.cdi.lang.model.tck.PlainClassMembers.Verifier.assertMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;

import jakarta.enterprise.lang.model.declarations.ClassInfo;
import jakarta.enterprise.lang.model.declarations.FieldInfo;
import jakarta.enterprise.lang.model.declarations.MethodInfo;
import jakarta.enterprise.lang.model.declarations.RecordComponentInfo;
import jakarta.enterprise.lang.model.types.PrimitiveType;
import jakarta.enterprise.lang.model.types.Type;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.RECORD_COMPONENT, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE_USE })
@interface AnnRecordComponent1 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.RECORD_COMPONENT, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE_USE })
@interface AnnRecordComponent2 {
}

@Repeatable(AnnRecordComponentRepeatableContainer.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.RECORD_COMPONENT, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE_USE })
@interface AnnRecordComponentRepeatable {
    String value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.RECORD_COMPONENT, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE_USE })
@interface AnnRecordComponentRepeatableContainer {
    AnnRecordComponentRepeatable[] value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@interface AnnRecordConstructor1 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@interface AnnRecordConstructor2 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@interface AnnRecordConstructor3 {
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@interface AnnRecordConstructor4 {
}

@Repeatable(AnnRecordConstructorRepeatableContainer.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@interface AnnRecordConstructorRepeatable {
    String value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@interface AnnRecordConstructorRepeatableContainer {
    AnnRecordConstructorRepeatable[] value();
}

interface RecordInterface {
    void interfaceMethod();
}

public record RecordMembers(
        @AnnRecordComponent1 @AnnRecordComponentRepeatable("foo") @AnnRecordComponentRepeatable("bar") String s,
        @AnnRecordComponent2 @AnnRecordComponentRepeatable("baz") Integer i) implements RecordInterface {

    // fields

    public static final String publicStaticFinalField = "";
    public static String publicStaticField;

    protected static final String protectedStaticFinalField = "";
    protected static String protectedStaticField;

    static final String packagePrivateStaticFinalField = "";
    static String packagePrivateStaticField;

    private static final String privateStaticFinalField = "";
    private static String privateStaticField;

    // methods

    public static final void publicStaticFinalMethod() {
    }

    public static void publicStaticMethod() {
    }

    public final void publicFinalMethod() {
    }

    public void publicMethod() {
    }

    protected static final void protectedStaticFinalMethod() {
    }

    protected static void protectedStaticMethod() {
    }

    protected final void protectedFinalMethod() {
    }

    protected void protectedMethod() {
    }

    static final void packagePrivateStaticFinalMethod() {
    }

    static void packagePrivateStaticMethod() {
    }

    final void packagePrivateFinalMethod() {
    }

    void packagePrivateMethod() {
    }

    private static final void privateStaticFinalMethod() {
    }

    private static void privateStaticMethod() {
    }

    private final void privateFinalMethod() {
    }

    private void privateMethod() {
    }

    // constructors

    @AnnRecordConstructor1
    RecordMembers(@AnnRecordConstructor2 @AnnRecordConstructorRepeatable("foo") boolean disambiguate) {
        this("", 0);
    }

    @AnnRecordConstructor3
    private RecordMembers(
            @AnnRecordConstructor4 @AnnRecordConstructorRepeatable("bar") @AnnRecordConstructorRepeatable("baz") int disambiguate) {
        this("", 0);
    }

    // inherited

    @Override
    public void interfaceMethod() {
    }

    public static class Verifier {
        public static void verify(ClassInfo clazz) {
            ClassInfo superInterface = clazz.superInterfacesDeclarations().get(0);
            assert superInterface.isInterface();
            assert superInterface.methods().size() == 1;
            assertMethod(superInterface, "interfaceMethod", Modifier.PUBLIC, false, false, true);

            assert clazz.name().equals("org.jboss.cdi.lang.model.tck.RecordMembers");
            assert clazz.simpleName().equals("RecordMembers");

            assert !clazz.isPlainClass();
            assert !clazz.isInterface();
            assert !clazz.isEnum();
            assert !clazz.isAnnotation();
            assert clazz.isRecord();

            assert !clazz.isAbstract();
            assert clazz.isFinal(); // records are always final

            assert Modifier.isPublic(clazz.modifiers());
            assert !Modifier.isProtected(clazz.modifiers());
            assert !Modifier.isPrivate(clazz.modifiers());

            verifyFields(clazz);
            verifyMethods(clazz);
            verifyConstructors(clazz);
            verifyRecordComponents(clazz);
        }

        private static void verifyFields(ClassInfo clazz) {
            // 2 implicitly declared fields, corresponding to record components
            // 8 explicitly declared fields
            assert clazz.fields().size() == 10;

            // @AnnRecordComponent1 @AnnRecordComponentRepeatable("foo") @AnnRecordComponentRepeatable("bar") String s
            FieldInfo s = LangModelUtils.singleField(clazz, "s");
            assert s.equals(LangModelUtils.singleRecordComponent(clazz, "s").field());
            assert s.annotations().size() == 2;
            assert s.hasAnnotation(AnnRecordComponent1.class);
            assert s.hasAnnotation(AnnRecordComponentRepeatableContainer.class);
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).hasValue();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().isArray();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray().size() == 2;
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(0).isNestedAnnotation();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(0).asNestedAnnotation().hasValue();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(0).asNestedAnnotation().value().isString();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(0).asNestedAnnotation().value().asString().equals("foo");
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(1).isNestedAnnotation();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(1).asNestedAnnotation().hasValue();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(1).asNestedAnnotation().value().isString();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(1).asNestedAnnotation().value().asString().equals("bar");
            assert !s.hasAnnotation(AnnRecordComponentRepeatable.class);
            assert !s.hasAnnotation(MissingAnnotation.class);
            if (LangModelVerifier.RUN_TYPE_ANNOTATION_TESTS) {
                assert s.type().annotations().size() == 2;
                assert s.type().hasAnnotation(AnnRecordComponent1.class);
                assert s.type().hasAnnotation(AnnRecordComponentRepeatableContainer.class);
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).hasValue();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().isArray();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray().size() == 2;
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(0).isNestedAnnotation();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(0).asNestedAnnotation().hasValue();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(0).asNestedAnnotation().value().isString();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(0).asNestedAnnotation().value().asString().equals("foo");
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(1).isNestedAnnotation();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(1).asNestedAnnotation().hasValue();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(1).asNestedAnnotation().value().isString();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(1).asNestedAnnotation().value().asString().equals("bar");
                assert !s.type().hasAnnotation(AnnRecordComponentRepeatable.class);
                assert !s.type().hasAnnotation(MissingAnnotation.class);
            }

            // @AnnRecordComponent2 @AnnRecordComponentRepeatable("baz") Integer i
            FieldInfo i = LangModelUtils.singleField(clazz, "i");
            assert i.equals(LangModelUtils.singleRecordComponent(clazz, "i").field());
            assert i.annotations().size() == 2;
            assert i.hasAnnotation(AnnRecordComponent2.class);
            assert i.hasAnnotation(AnnRecordComponentRepeatable.class);
            assert i.annotation(AnnRecordComponentRepeatable.class).hasValue();
            assert i.annotation(AnnRecordComponentRepeatable.class).value().isString();
            assert i.annotation(AnnRecordComponentRepeatable.class).value().asString().equals("baz");
            assert !i.hasAnnotation(AnnRecordComponentRepeatableContainer.class);
            assert !i.hasAnnotation(MissingAnnotation.class);
            if (LangModelVerifier.RUN_TYPE_ANNOTATION_TESTS) {
                assert i.type().annotations().size() == 2;
                assert i.type().hasAnnotation(AnnRecordComponent2.class);
                assert i.type().hasAnnotation(AnnRecordComponentRepeatable.class);
                assert i.type().annotation(AnnRecordComponentRepeatable.class).hasValue();
                assert i.type().annotation(AnnRecordComponentRepeatable.class).value().isString();
                assert i.type().annotation(AnnRecordComponentRepeatable.class).value().asString().equals("baz");
                assert !i.type().hasAnnotation(AnnRecordComponentRepeatableContainer.class);
                assert !i.type().hasAnnotation(MissingAnnotation.class);
            }

            assertField(clazz, "s", Modifier.PRIVATE, false, true, true);
            assertField(clazz, "i", Modifier.PRIVATE, false, true, false);

            assertField(clazz, "publicStaticFinalField", Modifier.PUBLIC, true, true, true);
            assertField(clazz, "publicStaticField", Modifier.PUBLIC, true, false, true);

            assertField(clazz, "protectedStaticFinalField", Modifier.PROTECTED, true, true, true);
            assertField(clazz, "protectedStaticField", Modifier.PROTECTED, true, false, true);

            assertField(clazz, "packagePrivateStaticFinalField", 0, true, true, true);
            assertField(clazz, "packagePrivateStaticField", 0, true, false, true);

            assertField(clazz, "privateStaticFinalField", Modifier.PRIVATE, true, true, true);
            assertField(clazz, "privateStaticField", Modifier.PRIVATE, true, false, true);
        }

        private static void verifyMethods(ClassInfo clazz) {
            // 2 implicitly declared accessor methods, corresponding to record components
            // 16 explicitly declared methods (without `interfaceMethod`)
            // 2 occurences of explicitly declared `interfaceMethod`
            // some method declarations are inherited from java.lang.Record
            assert clazz.methods().size() >= 2 + 16 + 2;

            // @AnnRecordComponent1 @AnnRecordComponentRepeatable("foo") @AnnRecordComponentRepeatable("bar") String s
            MethodInfo s = LangModelUtils.singleMethod(clazz, "s");
            assert s.equals(LangModelUtils.singleRecordComponent(clazz, "s").accessor());
            assert s.annotations().size() == 2;
            assert s.hasAnnotation(AnnRecordComponent1.class);
            assert s.hasAnnotation(AnnRecordComponentRepeatableContainer.class);
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).hasValue();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().isArray();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray().size() == 2;
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(0).isNestedAnnotation();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(0).asNestedAnnotation().hasValue();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(0).asNestedAnnotation().value().isString();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(0).asNestedAnnotation().value().asString().equals("foo");
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(1).isNestedAnnotation();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(1).asNestedAnnotation().hasValue();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(1).asNestedAnnotation().value().isString();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(1).asNestedAnnotation().value().asString().equals("bar");
            assert !s.hasAnnotation(AnnRecordComponentRepeatable.class);
            assert !s.hasAnnotation(MissingAnnotation.class);
            if (LangModelVerifier.RUN_TYPE_ANNOTATION_TESTS) {
                assert s.returnType().annotations().size() == 2;
                assert s.returnType().hasAnnotation(AnnRecordComponent1.class);
                assert s.returnType().hasAnnotation(AnnRecordComponentRepeatableContainer.class);
                assert s.returnType().annotation(AnnRecordComponentRepeatableContainer.class).hasValue();
                assert s.returnType().annotation(AnnRecordComponentRepeatableContainer.class).value().isArray();
                assert s.returnType().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray().size() == 2;
                assert s.returnType().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(0).isNestedAnnotation();
                assert s.returnType().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(0).asNestedAnnotation().hasValue();
                assert s.returnType().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(0).asNestedAnnotation().value().isString();
                assert s.returnType().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(0).asNestedAnnotation().value().asString().equals("foo");
                assert s.returnType().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(1).isNestedAnnotation();
                assert s.returnType().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(1).asNestedAnnotation().hasValue();
                assert s.returnType().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(1).asNestedAnnotation().value().isString();
                assert s.returnType().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(1).asNestedAnnotation().value().asString().equals("bar");
                assert !s.returnType().hasAnnotation(AnnRecordComponentRepeatable.class);
                assert !s.returnType().hasAnnotation(MissingAnnotation.class);
            }

            // @AnnRecordComponent2 @AnnRecordComponentRepeatable("baz") Integer i
            MethodInfo i = LangModelUtils.singleMethod(clazz, "i");
            assert i.equals(LangModelUtils.singleRecordComponent(clazz, "i").accessor());
            assert i.annotations().size() == 2;
            assert i.hasAnnotation(AnnRecordComponent2.class);
            assert i.hasAnnotation(AnnRecordComponentRepeatable.class);
            assert i.annotation(AnnRecordComponentRepeatable.class).hasValue();
            assert i.annotation(AnnRecordComponentRepeatable.class).value().isString();
            assert i.annotation(AnnRecordComponentRepeatable.class).value().asString().equals("baz");
            assert !i.hasAnnotation(AnnRecordComponentRepeatableContainer.class);
            assert !i.hasAnnotation(MissingAnnotation.class);
            if (LangModelVerifier.RUN_TYPE_ANNOTATION_TESTS) {
                assert i.returnType().annotations().size() == 2;
                assert i.returnType().hasAnnotation(AnnRecordComponent2.class);
                assert i.returnType().hasAnnotation(AnnRecordComponentRepeatable.class);
                assert i.returnType().annotation(AnnRecordComponentRepeatable.class).hasValue();
                assert i.returnType().annotation(AnnRecordComponentRepeatable.class).value().isString();
                assert i.returnType().annotation(AnnRecordComponentRepeatable.class).value().asString().equals("baz");
                assert !i.returnType().hasAnnotation(AnnRecordComponentRepeatableContainer.class);
                assert !i.returnType().hasAnnotation(MissingAnnotation.class);
            }

            assertMethod(clazz, "s", Modifier.PUBLIC, false, false, false, false);
            assertMethod(clazz, "i", Modifier.PUBLIC, false, false, false, false);

            assertMethod(clazz, "publicStaticFinalMethod", Modifier.PUBLIC, true, true, false);
            assertMethod(clazz, "publicStaticMethod", Modifier.PUBLIC, true, false, false);
            assertMethod(clazz, "publicFinalMethod", Modifier.PUBLIC, false, true, false);
            assertMethod(clazz, "publicMethod", Modifier.PUBLIC, false, false, false);

            assertMethod(clazz, "protectedStaticFinalMethod", Modifier.PROTECTED, true, true, false);
            assertMethod(clazz, "protectedStaticMethod", Modifier.PROTECTED, true, false, false);
            assertMethod(clazz, "protectedFinalMethod", Modifier.PROTECTED, false, true, false);
            assertMethod(clazz, "protectedMethod", Modifier.PROTECTED, false, false, false);

            assertMethod(clazz, "packagePrivateStaticFinalMethod", 0, true, true, false);
            assertMethod(clazz, "packagePrivateStaticMethod", 0, true, false, false);
            assertMethod(clazz, "packagePrivateFinalMethod", 0, false, true, false);
            assertMethod(clazz, "packagePrivateMethod", 0, false, false, false);

            assertMethod(clazz, "privateStaticFinalMethod", Modifier.PRIVATE, true, true, false);
            assertMethod(clazz, "privateStaticMethod", Modifier.PRIVATE, true, false, false);
            assertMethod(clazz, "privateFinalMethod", Modifier.PRIVATE, false, true, false);
            assertMethod(clazz, "privateMethod", Modifier.PRIVATE, false, false, false);
        }

        private static void verifyConstructors(ClassInfo clazz) {
            // 1 implicitly declared canonical constructor
            // 2 explicitly declared constructors
            // constructors on the `java.lang.Record` superclass are not returned
            assert clazz.constructors().size() == 3;

            // @AnnRecordConstructor1
            // RecordMembers(@AnnRecordConstructor2 @AnnRecordConstructorRepeatable("foo") boolean disambiguate) {}
            MethodInfo ctor = clazz.constructors()
                    .stream()
                    .filter(it -> {
                        Type firstParamType = it.parameters().get(0).type();
                        return firstParamType.isPrimitive()
                                && firstParamType.asPrimitive().primitiveKind() == PrimitiveType.PrimitiveKind.BOOLEAN;
                    })
                    .findAny()
                    .get();
            assert ctor.annotations().size() == 1;
            assert ctor.hasAnnotation(AnnRecordConstructor1.class);
            assert !ctor.hasAnnotation(MissingAnnotation.class);
            assert ctor.parameters().size() == 1;
            assert ctor.parameters().get(0).annotations().size() == 2;
            assert ctor.parameters().get(0).hasAnnotation(AnnRecordConstructor2.class);
            assert ctor.parameters().get(0).hasAnnotation(AnnRecordConstructorRepeatable.class);
            assert !ctor.parameters().get(0).hasAnnotation(AnnRecordConstructorRepeatableContainer.class);
            assert ctor.parameters().get(0).repeatableAnnotation(AnnRecordConstructorRepeatable.class).size() == 1;
            if (LangModelVerifier.RUN_TYPE_ANNOTATION_TESTS) {
                assert ctor.parameters().get(0).type().annotations().size() == 2;
                assert ctor.parameters().get(0).type().hasAnnotation(AnnRecordConstructor2.class);
                assert ctor.parameters().get(0).type().hasAnnotation(AnnRecordConstructorRepeatable.class);
                assert !ctor.parameters().get(0).type().hasAnnotation(AnnRecordConstructorRepeatableContainer.class);
                assert ctor.parameters().get(0).type().repeatableAnnotation(AnnRecordConstructorRepeatable.class).size() == 1;
            }

            // @AnnRecordConstructor3
            // private RecordMembers(@AnnRecordConstructor4 @AnnRecordConstructorRepeatable("bar") @AnnRecordConstructorRepeatable("baz") int disambiguate) {}
            MethodInfo ctor2 = clazz.constructors()
                    .stream()
                    .filter(it -> {
                        Type firstParamType = it.parameters().get(0).type();
                        return firstParamType.isPrimitive()
                                && firstParamType.asPrimitive().primitiveKind() == PrimitiveType.PrimitiveKind.INT;
                    })
                    .findAny()
                    .get();
            assert ctor2.annotations().size() == 1;
            assert ctor2.hasAnnotation(AnnRecordConstructor3.class);
            assert !ctor2.hasAnnotation(MissingAnnotation.class);
            assert ctor2.parameters().size() == 1;
            assert ctor2.parameters().get(0).annotations().size() == 2;
            assert ctor2.parameters().get(0).hasAnnotation(AnnRecordConstructor4.class);
            assert !ctor2.parameters().get(0).hasAnnotation(AnnRecordConstructorRepeatable.class);
            assert ctor2.parameters().get(0).hasAnnotation(AnnRecordConstructorRepeatableContainer.class);
            assert ctor2.parameters().get(0).repeatableAnnotation(AnnRecordConstructorRepeatable.class).size() == 2;
            if (LangModelVerifier.RUN_TYPE_ANNOTATION_TESTS) {
                assert ctor2.parameters().get(0).type().annotations().size() == 2;
                assert ctor2.parameters().get(0).type().hasAnnotation(AnnRecordConstructor4.class);
                assert !ctor2.parameters().get(0).type().hasAnnotation(AnnRecordConstructorRepeatable.class);
                assert ctor2.parameters().get(0).type().hasAnnotation(AnnRecordConstructorRepeatableContainer.class);
                assert ctor2.parameters().get(0).type().repeatableAnnotation(AnnRecordConstructorRepeatable.class).size() == 2;
            }

            assertConstructor(clazz, PrimitiveType.PrimitiveKind.BOOLEAN, 0);
            assertConstructor(clazz, PrimitiveType.PrimitiveKind.INT, Modifier.PRIVATE);
        }

        private static void verifyRecordComponents(ClassInfo clazz) {
            assert clazz.recordComponents().size() == 2;

            // @AnnRecordComponent1 @AnnRecordComponentRepeatable("foo") @AnnRecordComponentRepeatable("bar") String s
            RecordComponentInfo s = LangModelUtils.singleRecordComponent(clazz, "s");
            assert s.name().equals("s");
            assert s.accessor() != null;
            assert s.accessor().name().equals("s");
            assert s.field() != null;
            assert s.field().name().equals("s");
            assert s.declaringRecord().equals(clazz);
            assert s.type().kind() == Type.Kind.CLASS;
            assert s.type().asClass().declaration().name().equals("java.lang.String");
            assert s.annotations().size() == 2;
            assert s.hasAnnotation(AnnRecordComponent1.class);
            assert s.hasAnnotation(AnnRecordComponentRepeatableContainer.class);
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).hasValue();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().isArray();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray().size() == 2;
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(0).isNestedAnnotation();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(0).asNestedAnnotation().hasValue();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(0).asNestedAnnotation().value().isString();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(0).asNestedAnnotation().value().asString().equals("foo");
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(1).isNestedAnnotation();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(1).asNestedAnnotation().hasValue();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(1).asNestedAnnotation().value().isString();
            assert s.annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                    .get(1).asNestedAnnotation().value().asString().equals("bar");
            assert !s.hasAnnotation(AnnRecordComponentRepeatable.class);
            assert !s.hasAnnotation(MissingAnnotation.class);
            if (LangModelVerifier.RUN_TYPE_ANNOTATION_TESTS) {
                assert s.type().annotations().size() == 2;
                assert s.type().hasAnnotation(AnnRecordComponent1.class);
                assert s.type().hasAnnotation(AnnRecordComponentRepeatableContainer.class);
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).hasValue();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().isArray();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray().size() == 2;
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(0).isNestedAnnotation();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(0).asNestedAnnotation().hasValue();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(0).asNestedAnnotation().value().isString();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(0).asNestedAnnotation().value().asString().equals("foo");
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(1).isNestedAnnotation();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(1).asNestedAnnotation().hasValue();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(1).asNestedAnnotation().value().isString();
                assert s.type().annotation(AnnRecordComponentRepeatableContainer.class).value().asArray()
                        .get(1).asNestedAnnotation().value().asString().equals("bar");
                assert !s.type().hasAnnotation(AnnRecordComponentRepeatable.class);
                assert !s.type().hasAnnotation(MissingAnnotation.class);
            }

            // @AnnRecordComponent2 @AnnRecordComponentRepeatable("baz") Integer i
            RecordComponentInfo i = LangModelUtils.singleRecordComponent(clazz, "i");
            assert i.name().equals("i");
            assert i.accessor() != null;
            assert i.accessor().name().equals("i");
            assert i.field() != null;
            assert i.field().name().equals("i");
            assert i.declaringRecord().equals(clazz);
            assert i.type().kind() == Type.Kind.CLASS;
            assert i.type().asClass().declaration().name().equals("java.lang.Integer");
            assert i.annotations().size() == 2;
            assert i.hasAnnotation(AnnRecordComponent2.class);
            assert i.hasAnnotation(AnnRecordComponentRepeatable.class);
            assert i.annotation(AnnRecordComponentRepeatable.class).hasValue();
            assert i.annotation(AnnRecordComponentRepeatable.class).value().isString();
            assert i.annotation(AnnRecordComponentRepeatable.class).value().asString().equals("baz");
            assert !i.hasAnnotation(AnnRecordComponentRepeatableContainer.class);
            assert !i.hasAnnotation(MissingAnnotation.class);
            if (LangModelVerifier.RUN_TYPE_ANNOTATION_TESTS) {
                assert i.type().annotations().size() == 2;
                assert i.type().hasAnnotation(AnnRecordComponent2.class);
                assert i.type().hasAnnotation(AnnRecordComponentRepeatable.class);
                assert i.type().annotation(AnnRecordComponentRepeatable.class).hasValue();
                assert i.type().annotation(AnnRecordComponentRepeatable.class).value().isString();
                assert i.type().annotation(AnnRecordComponentRepeatable.class).value().asString().equals("baz");
                assert !i.type().hasAnnotation(AnnRecordComponentRepeatableContainer.class);
                assert !i.type().hasAnnotation(MissingAnnotation.class);
            }
        }
    }
}
