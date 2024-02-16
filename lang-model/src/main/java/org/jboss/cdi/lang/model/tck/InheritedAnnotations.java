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

import jakarta.enterprise.lang.model.declarations.ClassInfo;
import jakarta.enterprise.lang.model.declarations.MethodInfo;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface AnnInherited1 {
}

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface AnnInherited2 {
}

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface AnnInherited3 {
}

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface AnnInherited4 {
}

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface AnnInherited5 {
}

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface AnnInherited6 {
}

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface AnnInherited7 {
}

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface AnnInherited8 {
}

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface AnnInherited9 {
}

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface AnnInherited10 {
}

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface AnnInherited11 {
}

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface AnnInherited12 {
}

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface AnnInheritedWithMember {
    String value();
}

@AnnInherited1
@AnnInheritedWithMember("foo")
class AnnotatedSuperSuperClass {
}

@AnnInherited2
@AnnInheritedWithMember("bar")
class AnnotatedSuperClass extends AnnotatedSuperSuperClass {
    @AnnInherited3
    public static void staticMethod() {
    }

    @AnnInherited4
    public void method() {
    }
}

@AnnInherited5
interface AnnotatedSuperInterface {
    @AnnInherited6
    default void defaultInterfaceMethod() {
    }

    @AnnInherited7
    void interfaceMethod();
}

@AnnInherited8
@AnnInheritedWithMember("baz")
public class InheritedAnnotations extends AnnotatedSuperClass implements AnnotatedSuperInterface {
    @AnnInherited9
    public static void staticMethod() {
    }

    @AnnInherited10
    @Override
    public void method() {
    }

    @AnnInherited11
    @Override
    public void defaultInterfaceMethod() {
    }

    @AnnInherited12
    @Override
    public void interfaceMethod() {
    }

    public static void verify(ClassInfo clazz) {
        assert clazz.annotations().size() == 4;
        assert clazz.hasAnnotation(AnnInherited1.class);
        assert clazz.hasAnnotation(AnnInherited2.class);
        assert clazz.hasAnnotation(AnnInherited8.class);
        assert !clazz.hasAnnotation(AnnInherited5.class);

        assert clazz.hasAnnotation(AnnInheritedWithMember.class);
        assert clazz.annotation(AnnInheritedWithMember.class).value().asString().equals("baz");

        MethodInfo staticMethod = LangModelUtils.singleDeclaredMethod(clazz, "staticMethod");
        assert staticMethod.annotations().size() == 1;
        assert staticMethod.hasAnnotation(AnnInherited9.class);
        assert !staticMethod.hasAnnotation(AnnInherited3.class);

        MethodInfo method = LangModelUtils.singleDeclaredMethod(clazz, "method");
        assert method.annotations().size() == 1;
        assert method.hasAnnotation(AnnInherited10.class);
        assert !method.hasAnnotation(AnnInherited4.class);

        MethodInfo defaultInterfaceMethod = LangModelUtils.singleDeclaredMethod(clazz, "defaultInterfaceMethod");
        assert defaultInterfaceMethod.annotations().size() == 1;
        assert defaultInterfaceMethod.hasAnnotation(AnnInherited11.class);
        assert !defaultInterfaceMethod.hasAnnotation(AnnInherited6.class);

        MethodInfo interfaceMethod = LangModelUtils.singleDeclaredMethod(clazz, "interfaceMethod");
        assert interfaceMethod.annotations().size() == 1;
        assert interfaceMethod.hasAnnotation(AnnInherited12.class);
        assert !interfaceMethod.hasAnnotation(AnnInherited7.class);
    }

    public static void main(String[] args) {
        for (Annotation annotation : InheritedAnnotations.class.getAnnotations()) {
            System.out.println(annotation);
        }
    }
}
