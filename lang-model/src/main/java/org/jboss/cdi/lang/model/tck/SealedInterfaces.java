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

sealed interface SealedInterface permits SubinterfaceOfSealedInterface, ImplementationOfSealedInterface {
}

non-sealed interface SubinterfaceOfSealedInterface extends SealedInterface {
}

final class ImplementationOfSealedInterface implements SealedInterface {
}

sealed interface SealedInterfaceWithoutPermits {
}

non-sealed interface SubinterfaceOfSealedInterfaceWithoutPermits extends SealedInterfaceWithoutPermits {
}

final class ImplementationOfSealedInterfaceWithoutPermits implements SealedInterfaceWithoutPermits {
}

public class SealedInterfaces {
    SealedInterface sealedInterface;
    SealedInterfaceWithoutPermits sealedInterfaceWithoutPermits;

    public static void verify(ClassInfo clazz) {
        verifySealedInterface(LangModelUtils.classOfField(clazz, "sealedInterface"));
        verifySealedInterfaceWithoutPermits(LangModelUtils.classOfField(clazz, "sealedInterfaceWithoutPermits"));
    }

    private static void verifySealedInterface(ClassInfo clazz) {
        assert clazz.isInterface();
        assert clazz.isAbstract();
        assert !clazz.isFinal();
        assert clazz.isSealed();
        assert clazz.permittedSubclasses().size() == 2;
        for (ClassInfo subclass : clazz.permittedSubclasses()) {
            switch (subclass.simpleName()) {
                case "SubinterfaceOfSealedInterface" -> {
                    assert subclass.isInterface();
                    assert !subclass.isFinal();
                    assert subclass.isAbstract();
                    assert !subclass.isSealed();
                }
                case "ImplementationOfSealedInterface" -> {
                    assert subclass.isPlainClass();
                    assert subclass.isFinal();
                    assert !subclass.isAbstract();
                    assert !subclass.isSealed();
                }
                default -> throw new AssertionError("Unexpected subclass " + subclass + " of " + clazz);
            }
        }
    }

    private static void verifySealedInterfaceWithoutPermits(ClassInfo clazz) {
        assert clazz.isInterface();
        assert clazz.isAbstract();
        assert !clazz.isFinal();
        assert clazz.isSealed();
        assert clazz.permittedSubclasses().size() == 2;
        for (ClassInfo subclass : clazz.permittedSubclasses()) {
            switch (subclass.simpleName()) {
                case "SubinterfaceOfSealedInterfaceWithoutPermits" -> {
                    assert subclass.isInterface();
                    assert subclass.isAbstract();
                    assert !subclass.isFinal();
                    assert !subclass.isSealed();
                }
                case "ImplementationOfSealedInterfaceWithoutPermits" -> {
                    assert subclass.isPlainClass();
                    assert !subclass.isAbstract();
                    assert subclass.isFinal();
                    assert !subclass.isSealed();
                }
                default -> throw new AssertionError("Unexpected subclass " + subclass + " of " + clazz);
            }
        }
    }
}
