package org.jboss.cdi.lang.model.tck;

import jakarta.enterprise.lang.model.declarations.ClassInfo;
import jakarta.enterprise.lang.model.types.PrimitiveType;

import java.lang.reflect.Modifier;

import static org.jboss.cdi.lang.model.tck.PlainClassMembers.Verifier.assertConstructor;
import static org.jboss.cdi.lang.model.tck.PlainClassMembers.Verifier.assertField;
import static org.jboss.cdi.lang.model.tck.PlainClassMembers.Verifier.assertMethod;

interface EnumInterface {
    void interfaceMethod();
}

public enum EnumMembers implements EnumInterface {
    FOO(true) {
        @Override
        public void publicMethod() {
        }

        @Override
        protected void protectedMethod() {
        }
    },
    BAR(1) {
        @Override
        void packagePrivateMethod() {
        }

        @Override
        public void interfaceMethod() {
        }
    },
    ;

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

    EnumMembers(boolean disambiguate) {}
    private EnumMembers(int disambiguate) {}

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

            assert clazz.name().equals("org.jboss.cdi.lang.model.tck.EnumMembers");
            assert clazz.simpleName().equals("EnumMembers");

            assert !clazz.isPlainClass();
            assert !clazz.isInterface();
            assert clazz.isEnum();
            assert !clazz.isAnnotation();
            assert !clazz.isRecord();

            assert !clazz.isAbstract();
            assert !clazz.isFinal();

            assert Modifier.isPublic(clazz.modifiers());
            assert !Modifier.isProtected(clazz.modifiers());
            assert !Modifier.isPrivate(clazz.modifiers());

            verifyFields(clazz);
            verifyMethods(clazz);
            verifyConstructors(clazz);
        }

        private static void verifyFields(ClassInfo clazz) {
            assert clazz.fields().size() >= 16; // some field declarations are inherited from java.lang.Enum

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

        private static void verifyMethods(ClassInfo clazz) {
            assert clazz.methods().size() >= 16 + 2; // some method declarations are inherited from java.lang.Enum; interfaceMethod is present twice

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
            assert clazz.constructors().size() == 2;

            assertConstructor(clazz, PrimitiveType.PrimitiveKind.BOOLEAN, Modifier.PRIVATE);
            assertConstructor(clazz, PrimitiveType.PrimitiveKind.INT, Modifier.PRIVATE);
        }
    }
}
