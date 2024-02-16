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

import jakarta.enterprise.lang.model.AnnotationInfo;
import jakarta.enterprise.lang.model.declarations.ClassInfo;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Inherited
@Repeatable(AnnRepeatableContainer.class)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnRepeatable {
    String value();
}

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface AnnRepeatableContainer {
    AnnRepeatable[] value();
}

@Repeatable(AnnRepeatableContainerMissing.class)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnRepeatableMissing {
    String value();
}

@Retention(RetentionPolicy.RUNTIME)
@interface AnnRepeatableContainerMissing {
    AnnRepeatableMissing[] value();
}

@AnnRepeatable("foo")
@AnnRepeatable("bar")
class SuperSuperSuperClassWithRepeatableAnnotation {
}

@AnnRepeatable("baz")
class SuperSuperClassWithRepeatableAnnotation extends SuperSuperSuperClassWithRepeatableAnnotation {
}

@AnnRepeatable("qux")
@AnnRepeatable("quux")
@AnnRepeatable("quuz")
class SuperClassWithRepeatableAnnotation extends SuperSuperClassWithRepeatableAnnotation {
}

class InheritedRepeatableAnnotations extends SuperClassWithRepeatableAnnotation {
}

@AnnRepeatable("single")
class SingleRepeatableAnnotation {
}

@AnnRepeatable("a")
@AnnRepeatableContainer({
        @AnnRepeatable("b"),
        @AnnRepeatable("c")
})
class MixedRepeatableAnnotations {
}

public class RepeatableAnnotations extends SuperClassWithRepeatableAnnotation {
    InheritedRepeatableAnnotations inheritedRepeatableAnnotations;
    SingleRepeatableAnnotation singleRepeatableAnnotation;
    MixedRepeatableAnnotations mixedRepeatableAnnotations;

    public static void verify(ClassInfo clazz) {
        ClassInfo inheritedClass = LangModelUtils.classOfField(clazz, "inheritedRepeatableAnnotations");
        verifyInheritedRepeatableAnnotations(inheritedClass);
        verifyInheritedRepeatableAnnotations(inheritedClass.superClassDeclaration());
        verifyInheritedRepeatableAnnotationsSuperSuperClass(inheritedClass.superClassDeclaration().superClassDeclaration());
        verifyInheritedRepeatableAnnotationsSuperSuperSuperClass(inheritedClass.superClassDeclaration().superClassDeclaration().superClassDeclaration());

        verifySingleRepeatableAnnotation(LangModelUtils.classOfField(clazz, "singleRepeatableAnnotation"));

        verifyMixedRepeatableAnnotations(LangModelUtils.classOfField(clazz, "mixedRepeatableAnnotations"));
    }

    // the assertions here may seem weird (and indeed inherited repeatable annotations are weird),
    // but they are consistent with reflection:
    //
    // ClassInfo.hasAnnotation()        ~~ Class.isAnnotationPresent()
    // ClassInfo.annotation()           ~~ Class.getAnnotation()
    // ClassInfo.repeatableAnnotation() ~~ Class.getAnnotationsByType()
    // ClassInfo.annotations()          ~~ Class.getAnnotations()

    private static void verifyInheritedRepeatableAnnotations(ClassInfo clazz) {
        assert clazz.annotations().size() == 2;

        assert clazz.hasAnnotation(AnnRepeatableContainer.class);
        assert clazz.hasAnnotation(AnnRepeatable.class);

        assert clazz.hasAnnotation(it -> it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnRepeatableContainer"));
        assert clazz.hasAnnotation(it -> it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnRepeatable"));

        assert clazz.annotation(AnnRepeatableContainer.class) != null;
        assert clazz.annotation(AnnRepeatableContainer.class).value().isArray();
        assert clazz.annotation(AnnRepeatableContainer.class).value().asArray().size() == 3;
        assert clazz.annotation(AnnRepeatableContainer.class).value().asArray().get(0).asNestedAnnotation().value().asString().equals("qux");
        assert clazz.annotation(AnnRepeatableContainer.class).value().asArray().get(1).asNestedAnnotation().value().asString().equals("quux");
        assert clazz.annotation(AnnRepeatableContainer.class).value().asArray().get(2).asNestedAnnotation().value().asString().equals("quuz");

        assert clazz.annotation(AnnRepeatable.class) != null;
        assert clazz.annotation(AnnRepeatable.class).value().isString();
        assert clazz.annotation(AnnRepeatable.class).value().asString().equals("baz");

        Collection<AnnotationInfo> anns = clazz.repeatableAnnotation(AnnRepeatable.class);
        assert anns.size() == 3;
        Set<String> annValues = anns.stream()
                .map(it -> it.value().asString())
                .collect(Collectors.toSet());
        assert annValues.size() == 3;
        assert annValues.contains("qux");
        assert annValues.contains("quux");
        assert annValues.contains("quuz");

        assert clazz.annotation(AnnRepeatableMissing.class) == null;
        assert clazz.repeatableAnnotation(AnnRepeatableMissing.class).isEmpty();

        assert clazz.annotation(MissingAnnotation.class) == null;
        assert clazz.repeatableAnnotation(MissingAnnotation.class).isEmpty();
    }

    private static void verifyInheritedRepeatableAnnotationsSuperSuperClass(ClassInfo clazz) {
        assert clazz.annotations().size() == 2;

        assert clazz.hasAnnotation(AnnRepeatableContainer.class);
        assert clazz.hasAnnotation(AnnRepeatable.class);

        assert clazz.hasAnnotation(it -> it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnRepeatableContainer"));
        assert clazz.hasAnnotation(it -> it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnRepeatable"));

        assert clazz.annotation(AnnRepeatableContainer.class) != null;
        assert clazz.annotation(AnnRepeatableContainer.class).value().isArray();
        assert clazz.annotation(AnnRepeatableContainer.class).value().asArray().size() == 2;
        assert clazz.annotation(AnnRepeatableContainer.class).value().asArray().get(0).asNestedAnnotation().value().asString().equals("foo");
        assert clazz.annotation(AnnRepeatableContainer.class).value().asArray().get(1).asNestedAnnotation().value().asString().equals("bar");

        assert clazz.annotation(AnnRepeatable.class) != null;
        assert clazz.annotation(AnnRepeatable.class).value().isString();
        assert clazz.annotation(AnnRepeatable.class).value().asString().equals("baz");

        Collection<AnnotationInfo> anns = clazz.repeatableAnnotation(AnnRepeatable.class);
        assert anns.size() == 1;
        Set<String> annValues = anns.stream()
                .map(it -> it.value().asString())
                .collect(Collectors.toSet());
        assert annValues.size() == 1;
        assert annValues.contains("baz");

        assert clazz.annotation(AnnRepeatableMissing.class) == null;
        assert clazz.repeatableAnnotation(AnnRepeatableMissing.class).isEmpty();

        assert clazz.annotation(MissingAnnotation.class) == null;
        assert clazz.repeatableAnnotation(MissingAnnotation.class).isEmpty();
    }

    private static void verifyInheritedRepeatableAnnotationsSuperSuperSuperClass(ClassInfo clazz) {
        assert clazz.annotations().size() == 1;

        assert clazz.hasAnnotation(AnnRepeatableContainer.class);
        assert !clazz.hasAnnotation(AnnRepeatable.class);

        assert clazz.hasAnnotation(it -> it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnRepeatableContainer"));
        assert !clazz.hasAnnotation(it -> it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnRepeatable"));

        assert clazz.annotation(AnnRepeatableContainer.class) != null;
        assert clazz.annotation(AnnRepeatableContainer.class).value().isArray();
        assert clazz.annotation(AnnRepeatableContainer.class).value().asArray().size() == 2;
        assert clazz.annotation(AnnRepeatableContainer.class).value().asArray().get(0).asNestedAnnotation().value().asString().equals("foo");
        assert clazz.annotation(AnnRepeatableContainer.class).value().asArray().get(1).asNestedAnnotation().value().asString().equals("bar");

        assert clazz.annotation(AnnRepeatable.class) == null;

        Collection<AnnotationInfo> anns = clazz.repeatableAnnotation(AnnRepeatable.class);
        assert anns.size() == 2;
        Set<String> annValues = anns.stream()
                .map(it -> it.value().asString())
                .collect(Collectors.toSet());
        assert annValues.size() == 2;
        assert annValues.contains("foo");
        assert annValues.contains("bar");

        assert clazz.annotation(AnnRepeatableMissing.class) == null;
        assert clazz.repeatableAnnotation(AnnRepeatableMissing.class).isEmpty();

        assert clazz.annotation(MissingAnnotation.class) == null;
        assert clazz.repeatableAnnotation(MissingAnnotation.class).isEmpty();
    }

    private static void verifySingleRepeatableAnnotation(ClassInfo clazz) {
        assert clazz.annotations().size() == 1;

        assert !clazz.hasAnnotation(AnnRepeatableContainer.class);
        assert clazz.hasAnnotation(AnnRepeatable.class);

        assert !clazz.hasAnnotation(it -> it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnRepeatableContainer"));
        assert clazz.hasAnnotation(it -> it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnRepeatable"));

        assert clazz.annotation(AnnRepeatableContainer.class) == null;

        assert clazz.annotation(AnnRepeatable.class) != null;
        assert clazz.annotation(AnnRepeatable.class).value().isString();
        assert clazz.annotation(AnnRepeatable.class).value().asString().equals("single");

        Collection<AnnotationInfo> anns = clazz.repeatableAnnotation(AnnRepeatable.class);
        assert anns.size() == 1;
        assert anns.iterator().next().value().asString().equals("single");

        assert clazz.annotation(AnnRepeatableMissing.class) == null;
        assert clazz.repeatableAnnotation(AnnRepeatableMissing.class).isEmpty();

        assert clazz.annotation(MissingAnnotation.class) == null;
        assert clazz.repeatableAnnotation(MissingAnnotation.class).isEmpty();
    }

    private static void verifyMixedRepeatableAnnotations(ClassInfo clazz) {
        assert clazz.annotations().size() == 2;

        assert clazz.hasAnnotation(AnnRepeatableContainer.class);
        assert clazz.hasAnnotation(AnnRepeatable.class);

        assert clazz.hasAnnotation(it -> it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnRepeatableContainer"));
        assert clazz.hasAnnotation(it -> it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnRepeatable"));

        assert clazz.annotation(AnnRepeatableContainer.class) != null;
        assert clazz.annotation(AnnRepeatableContainer.class).value().isArray();
        assert clazz.annotation(AnnRepeatableContainer.class).value().asArray().size() == 2;
        assert clazz.annotation(AnnRepeatableContainer.class).value().asArray().get(0).asNestedAnnotation().value().asString().equals("b");
        assert clazz.annotation(AnnRepeatableContainer.class).value().asArray().get(1).asNestedAnnotation().value().asString().equals("c");

        assert clazz.annotation(AnnRepeatable.class) != null;
        assert clazz.annotation(AnnRepeatable.class).value().isString();
        assert clazz.annotation(AnnRepeatable.class).value().asString().equals("a");

        Collection<AnnotationInfo> anns = clazz.repeatableAnnotation(AnnRepeatable.class);
        assert anns.size() == 3;
        Set<String> annValues = anns.stream()
                .map(it -> it.value().asString())
                .collect(Collectors.toSet());
        assert annValues.size() == 3;
        assert annValues.contains("a");
        assert annValues.contains("b");
        assert annValues.contains("c");

        assert clazz.annotation(AnnRepeatableMissing.class) == null;
        assert clazz.repeatableAnnotation(AnnRepeatableMissing.class).isEmpty();

        assert clazz.annotation(MissingAnnotation.class) == null;
        assert clazz.repeatableAnnotation(MissingAnnotation.class).isEmpty();
    }
}
