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
import jakarta.enterprise.lang.model.declarations.FieldInfo;
import jakarta.enterprise.lang.model.declarations.MethodInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class LangModelUtils {
    static ClassInfo classOfField(ClassInfo clazz, String fieldName) {
        return singleField(clazz, fieldName).type().asClass().declaration();
    }

    static FieldInfo singleField(ClassInfo clazz, String name) {
        FieldInfo result = null;
        for (FieldInfo field : clazz.fields()) {
            if (name.equals(field.name())) {
                if (result == null) {
                    result = field;
                } else {
                    throw new IllegalStateException("More than 1 declaration of field '" + name + "' on '" + clazz.simpleName() + "'");
                }
            }
        }
        if (result == null) {
            throw new IllegalStateException("No declaration of field '" + name + "' on '" + clazz.simpleName() + "'");
        }
        return result;
    }

    static FieldInfo singleDeclaredField(ClassInfo clazz, String name) {
        FieldInfo result = null;
        for (FieldInfo field : clazz.fields()) {
            if (!field.declaringClass().equals(clazz)) {
                continue;
            }

            if (name.equals(field.name())) {
                if (result == null) {
                    result = field;
                } else {
                    throw new IllegalStateException("More than 1 declaration of field '" + name + "' on '" + clazz.simpleName() + "'");
                }
            }
        }
        if (result == null) {
            throw new IllegalStateException("No declaration of field '" + name + "' on '" + clazz.simpleName() + "'");
        }
        return result;
    }

    static Collection<FieldInfo> collectFields(ClassInfo clazz, String name) {
        List<FieldInfo> result = new ArrayList<>();
        for (FieldInfo field : clazz.fields()) {
            if (field.name().equals(name)) {
                result.add(field);
            }
        }
        return result;
    }

    static MethodInfo singleMethod(ClassInfo clazz, String name) {
        MethodInfo result = null;
        for (MethodInfo method : clazz.methods()) {
            if (name.equals(method.name())) {
                if (result == null) {
                    result = method;
                } else {
                    throw new IllegalStateException("More than 1 declaration of method '" + name + "' on '" + clazz.simpleName() + "'");
                }
            }
        }
        if (result == null) {
            throw new IllegalStateException("No declaration of method '" + name + "' on '" + clazz.simpleName() + "'");
        }
        return result;
    }

    static MethodInfo singleDeclaredMethod(ClassInfo clazz, String name) {
        MethodInfo result = null;
        for (MethodInfo method : clazz.methods()) {
            if (!method.declaringClass().equals(clazz)) {
                continue;
            }

            if (name.equals(method.name())) {
                if (result == null) {
                    result = method;
                } else {
                    throw new IllegalStateException("More than 1 declaration of method '" + name + "' on '" + clazz.simpleName() + "'");
                }
            }
        }
        if (result == null) {
            throw new IllegalStateException("No declaration of method '" + name + "' on '" + clazz.simpleName() + "'");
        }
        return result;
    }

    static Collection<MethodInfo> collectMethods(ClassInfo clazz, String name) {
        List<MethodInfo> result = new ArrayList<>();
        for (MethodInfo method : clazz.methods()) {
            if (method.name().equals(name)) {
                result.add(method);
            }
        }
        return result;
    }
}
