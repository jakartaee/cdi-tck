package org.jboss.cdi.lang.model.tck;

import jakarta.enterprise.lang.model.declarations.ClassInfo;

interface SuperSuperInterfaceWithFields {
    String interfaceField = "";
}

interface SuperInterfaceWithFields extends SuperSuperInterfaceWithFields {
    String interfaceField = "";
}

class SuperSuperClassWithFields {
    static String instanceField1 = "";

    String instanceField2 = "";
}

abstract class SuperClassWithFields extends SuperSuperClassWithFields implements SuperSuperInterfaceWithFields {
    static String instanceField1 = "";

    String instanceField2 = "";
}

public class InheritedFields extends SuperClassWithFields implements SuperInterfaceWithFields {
    String instanceField3 = "";

    public static class Verifier {
        public static void verify(ClassInfo clazz) {
            assert clazz.fields().size() == 7;

            assert LangModelUtils.collectFields(clazz, "interfaceField").size() == 2;
            assert LangModelUtils.collectFields(clazz, "instanceField1").size() == 2;
            assert LangModelUtils.collectFields(clazz, "instanceField2").size() == 2;
            assert LangModelUtils.collectFields(clazz, "instanceField3").size() == 1;
        }
    }
}
