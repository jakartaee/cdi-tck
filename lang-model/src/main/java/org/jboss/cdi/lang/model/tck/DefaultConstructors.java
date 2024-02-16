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
