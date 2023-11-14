package org.jboss.cdi.lang.model.tck;

import jakarta.enterprise.lang.model.declarations.ClassInfo;

public class JavaLangObjectMethods /*extends Object*/ {
    public static class Verifier {
        public static void verify(ClassInfo clazz) {
            assert clazz.methods().isEmpty();

            ClassInfo javaLangObject = clazz.superClassDeclaration();
            assert !javaLangObject.methods().isEmpty();
            assert LangModelUtils.collectMethods(javaLangObject, "toString").size() == 1;
        }
    }
}
