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

    static Collection<FieldInfo> collectFields(ClassInfo clazz, String methodName) {
        List<FieldInfo> result = new ArrayList<>();
        for (FieldInfo field : clazz.fields()) {
            if (field.name().equals(methodName)) {
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

    static Collection<MethodInfo> collectMethods(ClassInfo clazz, String methodName) {
        List<MethodInfo> result = new ArrayList<>();
        for (MethodInfo method : clazz.methods()) {
            if (method.name().equals(methodName)) {
                result.add(method);
            }
        }
        return result;
    }
}
