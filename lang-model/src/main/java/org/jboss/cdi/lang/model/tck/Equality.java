/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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

import jakarta.enterprise.lang.model.AnnotationTarget;
import jakarta.enterprise.lang.model.declarations.ClassInfo;
import jakarta.enterprise.lang.model.declarations.FieldInfo;
import jakarta.enterprise.lang.model.declarations.MethodInfo;
import jakarta.enterprise.lang.model.types.Type;

public class Equality {
    SimpleClass simpleClass;
    SimpleInterface simpleInterface;
    SimpleEnum simpleEnum;
    SimpleAnnotation simpleAnnotation;

    public static void verify(ClassInfo clazz) {
        verifyDeclarations(clazz);
        verifyTypes(clazz);
    }

    private static void verifyDeclarations(ClassInfo clazz) {
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
        assertInequality(simpleInterface, simpleClass);
        assertInequality(simpleInterface, simpleEnum);
        assertInequality(simpleInterface, simpleAnnotation);
        assertInequality(simpleEnum, simpleClass);
        assertInequality(simpleEnum, simpleInterface);
        assertInequality(simpleEnum, simpleAnnotation);
        assertInequality(simpleAnnotation, simpleClass);
        assertInequality(simpleAnnotation, simpleInterface);
        assertInequality(simpleAnnotation, simpleEnum);

        MethodInfo simpleMethod = LangModelUtils.singleDeclaredMethod(simpleClass, "simpleMethod");
        assertEquality(simpleMethod, LangModelUtils.singleDeclaredMethod(simpleClass, "simpleMethod"));
        assertInequality(simpleMethod, LangModelUtils.singleDeclaredMethod(simpleClass, "anotherMethod"));
        assertInequality(simpleMethod, LangModelUtils.singleDeclaredMethod(simpleInterface, "simpleMethod"));

        FieldInfo simpleStaticField = LangModelUtils.singleDeclaredField(simpleClass, "simpleStaticField");
        FieldInfo simpleField = LangModelUtils.singleDeclaredField(simpleClass, "simpleField");
        assertEquality(simpleStaticField, LangModelUtils.singleDeclaredField(simpleClass, "simpleStaticField"));
        assertEquality(simpleField, LangModelUtils.singleDeclaredField(simpleClass, "simpleField"));
        assertInequality(simpleStaticField, simpleField);
        assertInequality(simpleField, simpleStaticField);
        assertInequality(simpleStaticField, LangModelUtils.singleDeclaredField(simpleInterface, "simpleField"));
        assertInequality(simpleField, LangModelUtils.singleDeclaredField(simpleInterface, "simpleField"));
    }

    private static void verifyTypes(ClassInfo clazz) {
        Type simpleClass = LangModelUtils.singleField(clazz, "simpleClass").type();
        Type simpleInterface = LangModelUtils.singleField(clazz, "simpleInterface").type();
        Type simpleEnum = LangModelUtils.singleField(clazz, "simpleEnum").type();
        Type simpleAnnotation = LangModelUtils.singleField(clazz, "simpleAnnotation").type();

        assertEquality(simpleClass, LangModelUtils.singleField(clazz, "simpleClass").type());
        assertEquality(simpleInterface, LangModelUtils.singleField(clazz, "simpleInterface").type());
        assertEquality(simpleEnum, LangModelUtils.singleField(clazz, "simpleEnum").type());
        assertEquality(simpleAnnotation, LangModelUtils.singleField(clazz, "simpleAnnotation").type());

        assertInequality(simpleClass, simpleInterface);
        assertInequality(simpleClass, simpleEnum);
        assertInequality(simpleClass, simpleAnnotation);
        assertInequality(simpleInterface, simpleClass);
        assertInequality(simpleInterface, simpleEnum);
        assertInequality(simpleInterface, simpleAnnotation);
        assertInequality(simpleEnum, simpleClass);
        assertInequality(simpleEnum, simpleInterface);
        assertInequality(simpleEnum, simpleAnnotation);
        assertInequality(simpleAnnotation, simpleClass);
        assertInequality(simpleAnnotation, simpleInterface);
        assertInequality(simpleAnnotation, simpleEnum);

        Type simpleMethod = LangModelUtils.singleDeclaredMethod(simpleClass.asClass().declaration(), "simpleMethod").returnType();
        Type anotherMethod = LangModelUtils.singleDeclaredMethod(simpleClass.asClass().declaration(), "anotherMethod").returnType();
        assertEquality(simpleMethod, anotherMethod);
        assertEquality(anotherMethod, simpleMethod);

        Type simpleStaticField = LangModelUtils.singleDeclaredField(simpleClass.asClass().declaration(), "simpleStaticField").type();
        Type simpleField = LangModelUtils.singleDeclaredField(simpleClass.asClass().declaration(), "simpleField").type();
        assertEquality(simpleStaticField, simpleField);
        assertEquality(simpleField, simpleStaticField);

        assertInequality(simpleEnum, simpleField);
        assertInequality(simpleField, simpleEnum);
    }

    private static void assertEquality(AnnotationTarget a, AnnotationTarget b) {
        assert a.equals(b);
        assert a.hashCode() == b.hashCode();
    }

    private static void assertInequality(AnnotationTarget a, AnnotationTarget b) {
        assert !a.equals(b);
    }
}
