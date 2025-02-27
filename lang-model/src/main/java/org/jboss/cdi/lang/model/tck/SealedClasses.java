/*
 * Copyright 2025, Red Hat, Inc., and individual contributors
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

abstract sealed class SealedClass permits SubclassOfSealedClass1, SubclassOfSealedClass2, SubclassOfSealedClass3 {
}

final class SubclassOfSealedClass1 extends SealedClass {
}

non-sealed class SubclassOfSealedClass2 extends SealedClass {
}

sealed class SubclassOfSealedClass3 extends SealedClass permits SubclassOfSubclassOfSealedClass3 {
}

final class SubclassOfSubclassOfSealedClass3 extends SubclassOfSealedClass3 {
}

sealed class SealedClassWithoutPermits {
}

final class SubclassOfSealedClassWithoutPermits1 extends SealedClassWithoutPermits {
}

non-sealed class SubclassOfSealedClassWithoutPermits2 extends SealedClassWithoutPermits {
}

sealed class SubclassOfSealedClassWithoutPermits3 extends SealedClassWithoutPermits {
}

final class SubclassOfSubclassOfSealedClassWithoutPermits3 extends SubclassOfSealedClassWithoutPermits3 {
}

enum SealedEnum {
    FOO {
        @Override
        String hello() {
            return "FOO";
        }
    },
    BAR,
    BAZ {
        @Override
        String hello() {
            return "BAZ";
        }
    },
    QUUX,
    ;

    String hello() {
        return "unknown";
    }
}

public class SealedClasses {
    SealedClass sealedClass;
    SealedClassWithoutPermits sealedClassWithoutPermits;
    SealedEnum sealedEnum;

    public static void verify(ClassInfo clazz) {
        verifySealedClass(LangModelUtils.classOfField(clazz, "sealedClass"));
        verifySealedClassWithoutPermits(LangModelUtils.classOfField(clazz, "sealedClassWithoutPermits"));
        verifySealedEnum(LangModelUtils.classOfField(clazz, "sealedEnum"));
    }

    private static void verifySealedClass(ClassInfo clazz) {
        assert clazz.isPlainClass();
        assert clazz.isAbstract();
        assert !clazz.isFinal();
        assert clazz.isSealed();
        assert clazz.permittedSubclasses().size() == 3;
        for (ClassInfo subclass : clazz.permittedSubclasses()) {
            switch (subclass.simpleName()) {
                case "SubclassOfSealedClass1" -> {
                    assert subclass.isPlainClass();
                    assert subclass.isFinal();
                    assert !subclass.isAbstract();
                    assert !subclass.isSealed();
                }
                case "SubclassOfSealedClass2" -> {
                    assert subclass.isPlainClass();
                    assert !subclass.isFinal();
                    assert !subclass.isAbstract();
                    assert !subclass.isSealed();
                }
                case "SubclassOfSealedClass3" -> {
                    assert subclass.isPlainClass();
                    assert !subclass.isFinal();
                    assert !subclass.isAbstract();
                    assert subclass.isSealed();
                    assert subclass.permittedSubclasses().size() == 1;
                }
                default -> throw new AssertionError("Unexpected subclass " + subclass + " of " + clazz);
            }
        }
    }

    private static void verifySealedClassWithoutPermits(ClassInfo clazz) {
        assert clazz.isPlainClass();
        assert !clazz.isAbstract();
        assert !clazz.isFinal();
        assert clazz.isSealed();
        assert clazz.permittedSubclasses().size() == 3;
        for (ClassInfo subclass : clazz.permittedSubclasses()) {
            switch (subclass.simpleName()) {
                case "SubclassOfSealedClassWithoutPermits1" -> {
                    assert subclass.isPlainClass();
                    assert !subclass.isAbstract();
                    assert subclass.isFinal();
                    assert !subclass.isSealed();
                }
                case "SubclassOfSealedClassWithoutPermits2" -> {
                    assert subclass.isPlainClass();
                    assert !subclass.isAbstract();
                    assert !subclass.isFinal();
                    assert !subclass.isSealed();
                }
                case "SubclassOfSealedClassWithoutPermits3" -> {
                    assert subclass.isPlainClass();
                    assert !subclass.isAbstract();
                    assert !subclass.isFinal();
                    assert subclass.isSealed();
                    assert subclass.permittedSubclasses().size() == 1;
                }
                default -> throw new AssertionError("Unexpected subclass " + subclass + " of " + clazz);
            }
        }
    }

    private static void verifySealedEnum(ClassInfo clazz) {
        assert clazz.isEnum();
        assert !clazz.isAbstract();
        assert !clazz.isFinal();
        assert clazz.isSealed();
        assert clazz.permittedSubclasses().size() == 2;
        // permitted subclasses of an enum are anonymous
    }
}
