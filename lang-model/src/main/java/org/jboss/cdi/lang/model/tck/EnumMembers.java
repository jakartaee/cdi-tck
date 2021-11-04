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

        @Override
        public void publicAbstractMethod() {
        }

        @Override
        void packagePrivateAbstractMethod() {
        }

        @Override
        protected void protectedAbstractMethod() {
        }
    },
    BAR(1) {
        @Override
        void packagePrivateMethod() {
        }

        @Override
        public void interfaceMethod() {
        }

        @Override
        public void publicAbstractMethod() {
        }

        @Override
        void packagePrivateAbstractMethod() {
        }

        @Override
        protected void protectedAbstractMethod() {
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

    public abstract void publicAbstractMethod();
    protected abstract void protectedAbstractMethod();
    abstract void packagePrivateAbstractMethod();

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

            assert clazz.isAbstract(); // declares abstract methods
            assert !clazz.isFinal();

            assert Modifier.isPublic(clazz.modifiers());
            assert !Modifier.isProtected(clazz.modifiers());
            assert !Modifier.isPrivate(clazz.modifiers());

            verifyFields(clazz);
            verifyMethods(clazz);
            verifyConstructors(clazz);
        }

        private static void verifyFields(ClassInfo clazz) {
            // 16 explicitly declared fields
            // 2 public static final fields implicitly declared, corresponding to enum constants
            // some field declarations are inherited from `java.lang.Enum`
            assert clazz.fields().size() >= 16 + 2;

            assertField(clazz, "FOO", Modifier.PUBLIC, true, true, false);
            assertField(clazz, "BAR", Modifier.PUBLIC, true, true, false);

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

        private static void verifyMethods(ClassInfo clazz) {
            // 19 explicitly declared methods (without `interfaceMethod`)
            // 2 occurences of explicitly declared `interfaceMethod`
            // 2 implicitly declared static methods: `values` and `valueOf`
            // some method declarations are inherited from java.lang.Enum
            assert clazz.methods().size() >= 19 + 2 + 2;

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

            assertMethod(clazz, "publicAbstractMethod", Modifier.PUBLIC, false, false, true);
            assertMethod(clazz, "protectedAbstractMethod", Modifier.PROTECTED, false, false, true);
            assertMethod(clazz, "packagePrivateAbstractMethod", 0, false, false, true);

            // there should be 1 method called `values` and 2 methods called `valueOf`
            // but it's probably better to not rely on the shape of `java.lang.Enum`
            assert !LangModelUtils.collectMethods(clazz, "values").isEmpty();
            assert !LangModelUtils.collectMethods(clazz, "valueOf").isEmpty();
        }

        private static void verifyConstructors(ClassInfo clazz) {
            // 2 explicitly declared constructors
            // constructors on the `java.lang.Enum` superclass are not returned
            assert clazz.constructors().size() == 2;

            assertConstructor(clazz, PrimitiveType.PrimitiveKind.BOOLEAN, Modifier.PRIVATE);
            assertConstructor(clazz, PrimitiveType.PrimitiveKind.INT, Modifier.PRIVATE);
        }
    }
}
