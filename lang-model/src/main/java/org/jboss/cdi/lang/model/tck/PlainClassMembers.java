package org.jboss.cdi.lang.model.tck;

import jakarta.enterprise.lang.model.declarations.ClassInfo;
import jakarta.enterprise.lang.model.declarations.FieldInfo;
import jakarta.enterprise.lang.model.declarations.MethodInfo;
import jakarta.enterprise.lang.model.types.PrimitiveType;

import java.lang.reflect.Modifier;

abstract class PlainAbstractClass {
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

    public PlainClassMembers(boolean disambiguate) {}
    protected PlainClassMembers(int disambiguate) {}
    PlainClassMembers(double disambiguate) {}
    private PlainClassMembers(char disambiguate) {}

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
            assert clazz.fields().size() == 16;

            assertField(clazz, "publicStaticFinalField", Modifier.PUBLIC, true, true);
            assertField(clazz, "publicStaticField", Modifier.PUBLIC, true, false);
            assertField(clazz, "publicFinalField", Modifier.PUBLIC, false, true);
            assertField(clazz, "publicField", Modifier.PUBLIC, false, false);

            assertField(clazz, "protectedStaticFinalField", Modifier.PROTECTED, true, true);
            assertField(clazz, "protectedStaticField", Modifier.PROTECTED, true, false);
            assertField(clazz, "protectedFinalField", Modifier.PROTECTED, false, true);
            assertField(clazz, "protectedField", Modifier.PROTECTED, false, false);

            assertField(clazz, "packagePrivateStaticFinalField", 0, true, true);
            assertField(clazz, "packagePrivateStaticField", 0, true, false);
            assertField(clazz, "packagePrivateFinalField", 0, false, true);
            assertField(clazz, "packagePrivateField", 0, false, false);

            assertField(clazz, "privateStaticFinalField", Modifier.PRIVATE, true, true);
            assertField(clazz, "privateStaticField", Modifier.PRIVATE, true, false);
            assertField(clazz, "privateFinalField", Modifier.PRIVATE, false, true);
            assertField(clazz, "privateField", Modifier.PRIVATE, false, false);
        }

        static void assertField(ClassInfo clazz, String name, int accesibility, boolean isStatic, boolean isFinal) {
            FieldInfo field = LangModelUtils.singleField(clazz, name);

            assert field.declaringClass().equals(clazz);

            assert Modifier.isPublic(field.modifiers()) == Modifier.isPublic(accesibility);
            assert Modifier.isProtected(field.modifiers()) == Modifier.isProtected(accesibility);
            assert Modifier.isPrivate(field.modifiers()) == Modifier.isPrivate(accesibility);

            assert field.isFinal() == isFinal;
            assert Modifier.isFinal(field.modifiers()) == isFinal;

            assert field.isStatic() == isStatic;
            assert Modifier.isStatic(field.modifiers()) == isStatic;

            assert field.type().isClass();
            assert field.type().asClass().declaration().name().equals("java.lang.String");
        }

        private static void verifyMethods(ClassInfo clazz) {
            assert clazz.methods().size() == 16 + 2; // abstractMethod is present twice

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

            assert method.returnType().isVoid();
        }

        private static void verifyConstructors(ClassInfo clazz) {
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
        }
    }
}
