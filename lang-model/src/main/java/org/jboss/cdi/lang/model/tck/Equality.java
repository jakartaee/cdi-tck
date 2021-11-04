package org.jboss.cdi.lang.model.tck;

import jakarta.enterprise.lang.model.AnnotationTarget;
import jakarta.enterprise.lang.model.declarations.ClassInfo;
import jakarta.enterprise.lang.model.declarations.FieldInfo;
import jakarta.enterprise.lang.model.declarations.MethodInfo;

public class Equality {
    SimpleClass simpleClass;
    SimpleInterface simpleInterface;
    SimpleEnum simpleEnum;
    SimpleAnnotation simpleAnnotation;

    public static void verify(ClassInfo clazz) {
        ClassInfo simpleClass = LangModelUtils.classOfField(clazz, "simpleClass");
        ClassInfo simpleInterface = LangModelUtils.classOfField(clazz, "simpleInterface");
        ClassInfo simpleEnum = LangModelUtils.classOfField(clazz, "simpleEnum");
        ClassInfo simpleAnnotation = LangModelUtils.classOfField(clazz, "simpleAnnotation");

        assertEquality(simpleClass, LangModelUtils.classOfField(clazz, "simpleClass"));
        assertEquality(simpleInterface, LangModelUtils.classOfField(clazz, "simpleInterface"));
        assertEquality(simpleEnum, LangModelUtils.classOfField(clazz, "simpleEnum"));
        assertEquality(simpleAnnotation, LangModelUtils.classOfField(clazz, "simpleAnnotation"));

        assertInequality(simpleClass, simpleInterface);
        assertInequality(simpleClass, simpleEnum);
        assertInequality(simpleClass, simpleAnnotation);
        assertInequality(simpleInterface, simpleEnum);
        assertInequality(simpleInterface, simpleAnnotation);
        assertInequality(simpleEnum, simpleAnnotation);

        MethodInfo simpleMethod = LangModelUtils.singleDeclaredMethod(simpleClass, "simpleMethod");
        assertEquality(simpleMethod, LangModelUtils.singleDeclaredMethod(simpleClass, "simpleMethod"));
        assertInequality(simpleMethod, LangModelUtils.singleDeclaredMethod(simpleClass, "anotherMethod"));
        assertInequality(simpleMethod, LangModelUtils.singleDeclaredMethod(simpleInterface, "simpleMethod"));

        FieldInfo simpleStaticField = LangModelUtils.singleDeclaredField(simpleClass, "simpleStaticField");
        FieldInfo simpleField = LangModelUtils.singleDeclaredField(simpleClass, "simpleField");
        assertEquality(simpleStaticField, LangModelUtils.singleDeclaredField(simpleClass, "simpleStaticField"));
        assertEquality(simpleField, LangModelUtils.singleDeclaredField(simpleClass, "simpleField"));
        assertInequality(simpleStaticField, simpleField);
        assertInequality(simpleStaticField, LangModelUtils.singleDeclaredField(simpleInterface, "simpleField"));
        assertInequality(simpleField, LangModelUtils.singleDeclaredField(simpleInterface, "simpleField"));
    }

    private static void assertEquality(AnnotationTarget a, AnnotationTarget b) {
        assert a.equals(b);
        assert a.hashCode() == b.hashCode();
    }

    private static void assertInequality(AnnotationTarget a, AnnotationTarget b) {
        assert !a.equals(b);
    }
}
