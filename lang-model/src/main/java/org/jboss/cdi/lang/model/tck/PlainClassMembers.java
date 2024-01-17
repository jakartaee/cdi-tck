/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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

import jakarta.enterprise.lang.model.declarations.ClassInfo;
import jakarta.enterprise.lang.model.declarations.FieldInfo;
import jakarta.enterprise.lang.model.declarations.MethodInfo;
import jakarta.enterprise.lang.model.types.PrimitiveType;
import jakarta.enterprise.lang.model.types.Type;

import java.lang.reflect.Modifier;

abstract class PlainAbstractClass {
    PlainAbstractClass(boolean disambiguate) {
    }

    PlainAbstractClass(String disambiguate) {
    }

    abstract void abstractMethod();
}

public final class PlainClassMembers extends PlainAbstractClass {
    // fields

    public static final String publicStaticFinalField = "";
    public static String publicStaticField;
    public final String publicFinalField = "";
    public String publicField;

    protected static final String protectedStaticFinalField = "";
    protected static String protectedStaticField;
    protected final String protectedFinalField = "";
    protected String protectedField;

    static final String packagePrivateStaticFinalField = "";
    static String packagePrivateStaticField;
    final String packagePrivateFinalField = "";
    String packagePrivateField;

    private static final String privateStaticFinalField = "";
    private static String privateStaticField;
    private final String privateFinalField = "";
    private String privateField;

    // methods

    public static final void publicStaticFinalMethod() {}
    public static void publicStaticMethod() {}
    public final void publicFinalMethod() {}
    public void publicMethod() {}

    protected static final void protectedStaticFinalMethod() {}
    protected static void protectedStaticMethod() {}
    protected final void protectedFinalMethod() {}
    protected void protectedMethod() {}

    static final void packagePrivateStaticFinalMethod() {}
    static void packagePrivateStaticMethod() {}
    final void packagePrivateFinalMethod() {}
    void packagePrivateMethod() {}

    private static final void privateStaticFinalMethod() {}
    private static void privateStaticMethod() {}
    private final void privateFinalMethod() {}
    private void privateMethod() {}

    // constructors

    public PlainClassMembers(boolean disambiguate) {
        super(false);
    }
    protected PlainClassMembers(int disambiguate) {
        super(false);
    }
    PlainClassMembers(double disambiguate) {
        super(false);
    }
    private PlainClassMembers(char disambiguate) {
        super(false);
    }

    // inherited

    @Override
    public void abstractMethod() {
    }

    public static class Verifier {
        public static void verify(ClassInfo clazz) {
            ClassInfo superClass = clazz.superClassDeclaration();
            assert superClass.isPlainClass();
            assert superClass.isAbstract();
            assert !superClass.isFinal();
            assert superClass.methods().size() == 1;
            assertMethod(superClass, "abstractMethod", 0, false, false, true);

            assert clazz.name().equals("org.jboss.cdi.lang.model.tck.PlainClassMembers");
            assert clazz.simpleName().equals("PlainClassMembers");

            assert clazz.isPlainClass();
            assert !clazz.isInterface();
            assert !clazz.isEnum();
            assert !clazz.isAnnotation();
            assert !clazz.isRecord();

            assert !clazz.isAbstract();
            assert clazz.isFinal();

            assert Modifier.isPublic(clazz.modifiers());
            assert !Modifier.isProtected(clazz.modifiers());
            assert !Modifier.isPrivate(clazz.modifiers());

            verifyFields(clazz);
            verifyMethods(clazz);
            verifyConstructors(clazz);
        }

        private static void verifyFields(ClassInfo clazz) {
            // 16 explicitly declared fields
            assert clazz.fields().size() == 16;

            assertField(clazz, "publicStaticFinalField", Modifier.PUBLIC, true, true, true);
            assertField(clazz, "publicStaticField", Modifier.PUBLIC, true, false, true);
            assertField(clazz, "publicFinalField", Modifier.PUBLIC, false, true, true);
            assertField(clazz, "publicField", Modifier.PUBLIC, false, false, true);

            assertField(clazz, "protectedStaticFinalField", Modifier.PROTECTED, true, true, true);
            assertField(clazz, "protectedStaticField", Modifier.PROTECTED, true, false, true);
            assertField(clazz, "protectedFinalField", Modifier.PROTECTED, false, true, true);
            assertField(clazz, "protectedField", Modifier.PROTECTED, false, false, true);

            assertField(clazz, "packagePrivateStaticFinalField", 0, true, true, true);
            assertField(clazz, "packagePrivateStaticField", 0, true, false, true);
            assertField(clazz, "packagePrivateFinalField", 0, false, true, true);
            assertField(clazz, "packagePrivateField", 0, false, false, true);

            assertField(clazz, "privateStaticFinalField", Modifier.PRIVATE, true, true, true);
            assertField(clazz, "privateStaticField", Modifier.PRIVATE, true, false, true);
            assertField(clazz, "privateFinalField", Modifier.PRIVATE, false, true, true);
            assertField(clazz, "privateField", Modifier.PRIVATE, false, false, true);
        }

        static void assertField(ClassInfo clazz, String name, int accesibility, boolean isStatic, boolean isFinal, boolean testStringType) {
            FieldInfo field = LangModelUtils.singleField(clazz, name);

            assert field.declaringClass().equals(clazz);

            assert Modifier.isPublic(field.modifiers()) == Modifier.isPublic(accesibility);
            assert Modifier.isProtected(field.modifiers()) == Modifier.isProtected(accesibility);
            assert Modifier.isPrivate(field.modifiers()) == Modifier.isPrivate(accesibility);

            assert field.isFinal() == isFinal;
            assert Modifier.isFinal(field.modifiers()) == isFinal;

            assert field.isStatic() == isStatic;
            assert Modifier.isStatic(field.modifiers()) == isStatic;

            assertType(field.type(), Type.Kind.CLASS);
            if (testStringType) {
                assert field.type().asClass().declaration().name().equals("java.lang.String");
            }
        }

        private static void verifyMethods(ClassInfo clazz) {
            // 16 explicitly declared methods (without `abstractMethod`)
            // 2 occurences of explicitly declared `abstractMethod`
            assert clazz.methods().size() == 16 + 2;

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

        static void assertMethod(ClassInfo clazz, String name, int accesibility, boolean isStatic, boolean isFinal, boolean isAbstract) {
            MethodInfo method = LangModelUtils.singleMethod(clazz, name);

            assert method.declaringClass().equals(clazz);

            assert !method.isConstructor();

            assert Modifier.isPublic(method.modifiers()) == Modifier.isPublic(accesibility);
            assert Modifier.isProtected(method.modifiers()) == Modifier.isProtected(accesibility);
            assert Modifier.isPrivate(method.modifiers()) == Modifier.isPrivate(accesibility);

            assert method.isFinal() == isFinal;
            assert Modifier.isFinal(method.modifiers()) == isFinal;

            assert method.isStatic() == isStatic;
            assert Modifier.isStatic(method.modifiers()) == isStatic;

            assert method.isAbstract() == isAbstract;
            assert Modifier.isAbstract(method.modifiers()) == isAbstract;

            assertType(method.returnType(), Type.Kind.VOID);
        }

        private static void verifyConstructors(ClassInfo clazz) {
            // 4 explicitly declared constructors
            // constructors on the superclass are not returned
            assert clazz.constructors().size() == 4;

            assertConstructor(clazz, PrimitiveType.PrimitiveKind.BOOLEAN, Modifier.PUBLIC);
            assertConstructor(clazz, PrimitiveType.PrimitiveKind.INT, Modifier.PROTECTED);
            assertConstructor(clazz, PrimitiveType.PrimitiveKind.DOUBLE, 0);
            assertConstructor(clazz, PrimitiveType.PrimitiveKind.CHAR, Modifier.PRIVATE);
        }

        static void assertConstructor(ClassInfo clazz, PrimitiveType.PrimitiveKind paramType, int accesibility) {
            MethodInfo ctor = clazz.constructors()
                    .stream()
                    .filter(it -> it.parameters().get(0).type().asPrimitive().primitiveKind() == paramType)
                    .findAny()
                    .get();

            assert ctor.name().equals(clazz.name());

            assert ctor.declaringClass().equals(clazz);

            assert ctor.isConstructor();

            assert Modifier.isPublic(ctor.modifiers()) == Modifier.isPublic(accesibility);
            assert Modifier.isProtected(ctor.modifiers()) == Modifier.isProtected(accesibility);
            assert Modifier.isPrivate(ctor.modifiers()) == Modifier.isPrivate(accesibility);

            assert !ctor.isStatic();
            assert !ctor.isAbstract();
            assert !ctor.isFinal();

            assert ctor.returnType().asClass().declaration().equals(clazz);

            assert ctor.parameters().size() == 1;
        }

        static void assertType(Type type, Type.Kind expectedKind) {
            assert type.kind() == expectedKind;

            assert type.isVoid() == (expectedKind == Type.Kind.VOID);
            assert type.isPrimitive() == (expectedKind == Type.Kind.PRIMITIVE);
            assert type.isClass() == (expectedKind == Type.Kind.CLASS);
            assert type.isArray() == (expectedKind == Type.Kind.ARRAY);
            assert type.isParameterizedType() == (expectedKind == Type.Kind.PARAMETERIZED_TYPE);
            assert type.isTypeVariable() == (expectedKind == Type.Kind.TYPE_VARIABLE);
            assert type.isWildcardType() == (expectedKind == Type.Kind.WILDCARD_TYPE);
        }
    }
}
