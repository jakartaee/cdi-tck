package org.jboss.cdi.lang.model.tck;

import jakarta.enterprise.lang.model.declarations.ClassInfo;

interface SuperSuperInterfaceWithMethods {
    static String interfaceStaticMethod1() {
        return null;
    }

    private static String interfaceStaticMethod2() {
        return null;
    }

    String interfaceMethod1();

    default String interfaceMethod2() {
        return null;
    }

    private String interfaceMethod3() {
        return null;
    }
}

interface SuperInterfaceWithMethods extends SuperSuperInterfaceWithMethods {
    static String interfaceStaticMethod1() {
        return null;
    }

    private static String interfaceStaticMethod2() {
        return null;
    }

    @Override
    String interfaceMethod1();

    @Override
    default String interfaceMethod2() {
        return null;
    }

    private String interfaceMethod3() {
        return null;
    }
}

class SuperSuperClassWithMethods {
    public static void staticMethod() {
    }

    private String instanceMethod1() {
        return null;
    }

    public String instanceMethod2() {
        return null;
    }
}

abstract class SuperClassWithMethods extends SuperSuperClassWithMethods implements SuperSuperInterfaceWithMethods {
    public static void staticMethod() {
    }

    private String instanceMethod1() {
        return null;
    }

    @Override
    public String instanceMethod2() {
        return null;
    }
}

public class InheritedMethods extends SuperClassWithMethods implements SuperInterfaceWithMethods {
    @Override
    public String interfaceMethod1() {
        return null;
    }

    @Override
    public String interfaceMethod2() {
        return null;
    }

    public String instanceMethod3() {
        return null;
    }

    public static class Verifier {
        public static void verify(ClassInfo clazz) {
            assert clazz.methods().size() == 19;

            assert LangModelUtils.collectMethods(clazz, "interfaceStaticMethod1").size() == 2;
            assert LangModelUtils.collectMethods(clazz, "interfaceStaticMethod2").size() == 2;

            assert LangModelUtils.collectMethods(clazz, "interfaceMethod1").size() == 3;
            assert LangModelUtils.collectMethods(clazz, "interfaceMethod2").size() == 3;
            assert LangModelUtils.collectMethods(clazz, "interfaceMethod3").size() == 2;

            assert LangModelUtils.collectMethods(clazz, "staticMethod").size() == 2;

            assert LangModelUtils.collectMethods(clazz, "instanceMethod1").size() == 2;
            assert LangModelUtils.collectMethods(clazz, "instanceMethod2").size() == 2;
            assert LangModelUtils.collectMethods(clazz, "instanceMethod3").size() == 1;
        }
    }
}
