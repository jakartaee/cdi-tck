/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
