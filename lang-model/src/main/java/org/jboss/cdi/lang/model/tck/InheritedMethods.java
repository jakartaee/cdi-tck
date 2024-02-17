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

interface SuperSuperInterfaceWithMethods {
    static String interfaceStaticMethod1() {
        return null;
    }

    private static String interfaceStaticMethod2() {
        return null;
    }

    String interfaceMethod1();

    default String interfaceMethod2() {
        return null;
    }

    private String interfaceMethod3() {
        return null;
    }
}

interface SuperInterfaceWithMethods extends SuperSuperInterfaceWithMethods {
    static String interfaceStaticMethod1() {
        return null;
    }

    private static String interfaceStaticMethod2() {
        return null;
    }

    @Override
    String interfaceMethod1();

    @Override
    default String interfaceMethod2() {
        return null;
    }

    private String interfaceMethod3() {
        return null;
    }
}

class SuperSuperClassWithMethods {
    public static void staticMethod() {
    }

    private String instanceMethod1() {
        return null;
    }

    public String instanceMethod2() {
        return null;
    }
}

abstract class SuperClassWithMethods extends SuperSuperClassWithMethods implements SuperSuperInterfaceWithMethods {
    public static void staticMethod() {
    }

    private String instanceMethod1() {
        return null;
    }

    @Override
    public String instanceMethod2() {
        return null;
    }
}

public class InheritedMethods extends SuperClassWithMethods implements SuperInterfaceWithMethods {
    @Override
    public String interfaceMethod1() {
        return null;
    }

    @Override
    public String interfaceMethod2() {
        return null;
    }

    public String instanceMethod3() {
        return null;
    }

    public static class Verifier {
        public static void verify(ClassInfo clazz) {
            assert clazz.methods().size() == 19;

            assert LangModelUtils.collectMethods(clazz, "interfaceStaticMethod1").size() == 2;
            assert LangModelUtils.collectMethods(clazz, "interfaceStaticMethod1").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperSuperInterfaceWithMethods"))
                    .count() == 1;
            assert LangModelUtils.collectMethods(clazz, "interfaceStaticMethod1").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperInterfaceWithMethods"))
                    .count() == 1;

            assert LangModelUtils.collectMethods(clazz, "interfaceStaticMethod2").size() == 2;
            assert LangModelUtils.collectMethods(clazz, "interfaceStaticMethod2").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperSuperInterfaceWithMethods"))
                    .count() == 1;
            assert LangModelUtils.collectMethods(clazz, "interfaceStaticMethod2").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperInterfaceWithMethods"))
                    .count() == 1;

            assert LangModelUtils.collectMethods(clazz, "interfaceMethod1").size() == 3;
            assert LangModelUtils.collectMethods(clazz, "interfaceMethod1").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperSuperInterfaceWithMethods"))
                    .count() == 1;
            assert LangModelUtils.collectMethods(clazz, "interfaceMethod1").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperInterfaceWithMethods"))
                    .count() == 1;
            assert LangModelUtils.collectMethods(clazz, "interfaceMethod1").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("InheritedMethods"))
                    .count() == 1;

            assert LangModelUtils.collectMethods(clazz, "interfaceMethod2").size() == 3;
            assert LangModelUtils.collectMethods(clazz, "interfaceMethod2").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperSuperInterfaceWithMethods"))
                    .count() == 1;
            assert LangModelUtils.collectMethods(clazz, "interfaceMethod2").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperInterfaceWithMethods"))
                    .count() == 1;
            assert LangModelUtils.collectMethods(clazz, "interfaceMethod2").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("InheritedMethods"))
                    .count() == 1;

            assert LangModelUtils.collectMethods(clazz, "interfaceMethod3").size() == 2;
            assert LangModelUtils.collectMethods(clazz, "interfaceMethod3").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperSuperInterfaceWithMethods"))
                    .count() == 1;
            assert LangModelUtils.collectMethods(clazz, "interfaceMethod3").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperInterfaceWithMethods"))
                    .count() == 1;

            assert LangModelUtils.collectMethods(clazz, "staticMethod").size() == 2;
            assert LangModelUtils.collectMethods(clazz, "staticMethod").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperSuperClassWithMethods"))
                    .count() == 1;
            assert LangModelUtils.collectMethods(clazz, "staticMethod").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperClassWithMethods"))
                    .count() == 1;

            assert LangModelUtils.collectMethods(clazz, "instanceMethod1").size() == 2;
            assert LangModelUtils.collectMethods(clazz, "instanceMethod1").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperSuperClassWithMethods"))
                    .count() == 1;
            assert LangModelUtils.collectMethods(clazz, "instanceMethod1").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperClassWithMethods"))
                    .count() == 1;

            assert LangModelUtils.collectMethods(clazz, "instanceMethod2").size() == 2;
            assert LangModelUtils.collectMethods(clazz, "instanceMethod2").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperSuperClassWithMethods"))
                    .count() == 1;
            assert LangModelUtils.collectMethods(clazz, "instanceMethod2").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("SuperClassWithMethods"))
                    .count() == 1;

            assert LangModelUtils.collectMethods(clazz, "instanceMethod3").size() == 1;
            assert LangModelUtils.collectMethods(clazz, "instanceMethod3").stream()
                    .filter(it -> it.declaringClass().simpleName().equals("InheritedMethods"))
                    .count() == 1;
        }
    }
}
