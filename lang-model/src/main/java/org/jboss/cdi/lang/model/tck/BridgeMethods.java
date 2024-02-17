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

class BridgeMethodsSuperClass {
    Number numberMethod() {
        return null;
    }
}

class BridgeMethodsClass extends BridgeMethodsSuperClass {
    // bridge method `Number numberMethod()` will be generated

    @Override
    Integer numberMethod() {
        return null;
    }
}

class BridgeMethodsGenericSuperClass<T> {
    void genericMethod(T t) {
    }
}

class BridgeMethodsGenericClass extends BridgeMethodsGenericSuperClass<String> {
    // bridge method `void genericMethod(Object)` will be generated

    @Override
    void genericMethod(String s) {
    }
}

public class BridgeMethods {
    BridgeMethodsClass covariantReturnTypes;
    BridgeMethodsGenericClass generics;

    public static void verify(ClassInfo clazz) {
        verifyCovariantReturnTypes(LangModelUtils.classOfField(clazz, "covariantReturnTypes"));
        verifyGenerics(LangModelUtils.classOfField(clazz, "generics"));
    }

    private static void verifyCovariantReturnTypes(ClassInfo clazz) {
        assert clazz.methods().size() == 2;

        assert clazz.methods()
                .stream()
                .filter(it -> "numberMethod".equals(it.name()))
                .filter(it -> it.returnType().asClass().declaration().name().equals("java.lang.Number"))
                .count() == 1;

        assert clazz.methods()
                .stream()
                .filter(it -> "numberMethod".equals(it.name()))
                .filter(it -> it.returnType().asClass().declaration().name().equals("java.lang.Integer"))
                .count() == 1;
    }

    private static void verifyGenerics(ClassInfo clazz) {
        assert clazz.methods().size() == 2;

        assert clazz.methods()
                .stream()
                .filter(it -> {
                    return "genericMethod".equals(it.name())
                            && it.parameters().size() == 1
                            && it.parameters().get(0).type().isTypeVariable()
                            && it.parameters().get(0).type().asTypeVariable().name().equals("T");
                })
                .count() == 1;

        assert clazz.methods()
                .stream()
                .filter(it -> {
                    return "genericMethod".equals(it.name())
                            && it.parameters().size() == 1
                            && it.parameters().get(0).type().isClass()
                            && it.parameters().get(0).type().asClass().declaration().name().equals("java.lang.String");
                })
                .count() == 1;
    }
}
