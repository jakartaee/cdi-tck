package org.jboss.cdi.lang.model.tck;

import jakarta.enterprise.lang.model.declarations.ClassInfo;

class ClassWithDefaultConstructor {
}

enum EnumWithDefaultConstructor {
    SINGLETON
}

public class DefaultConstructors {
    ClassWithDefaultConstructor classWithDefaultConstructor;
    EnumWithDefaultConstructor enumWithDefaultConstructor;

    public static void verify(ClassInfo clazz) {
        assert LangModelUtils.classOfField(clazz, "classWithDefaultConstructor").constructors().size() == 1;
        assert LangModelUtils.classOfField(clazz, "enumWithDefaultConstructor").constructors().size() == 1;
    }
}
