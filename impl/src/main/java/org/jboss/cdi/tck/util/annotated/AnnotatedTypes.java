/*
 * Copyright 2017, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.util.annotated;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.AnnotatedCallable;
import jakarta.enterprise.inject.spi.AnnotatedConstructor;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedParameter;
import jakarta.enterprise.inject.spi.AnnotatedType;

/**
 * This class originally comes from Weld utility class
 */
public class AnnotatedTypes {

    /**
     * compares two annotated elements to see if they have the same annotations
     *
     * @param a1
     * @param a2
     * @return
     */
    private static boolean compareAnnotated(Annotated a1, Annotated a2) {
        return a1.getAnnotations().equals(a2.getAnnotations());
    }

    public static boolean compareAnnotatedField(AnnotatedField<?> f1, AnnotatedField<?> f2) {
        if (!f1.getJavaMember().equals(f2.getJavaMember())) {
            return false;
        }
        return compareAnnotated(f1, f2);
    }

    public static boolean compareAnnotatedCallable(AnnotatedCallable<?> m1, AnnotatedCallable<?> m2) {
        if (!m1.getJavaMember().equals(m2.getJavaMember())) {
            return false;
        }
        if (!compareAnnotated(m1, m2)) {
            return false;
        }
        return compareAnnotatedParameters(m1.getParameters(), m2.getParameters());
    }

    /**
     * compares two annotated elements to see if they have the same annotations
     */
    public static boolean compareAnnotatedParameters(List<? extends AnnotatedParameter<?>> p1, List<? extends AnnotatedParameter<?>> p2) {
        if (p1.size() != p2.size()) {
            return false;
        }
        for (int i = 0; i < p1.size(); ++i) {
            if (!compareAnnotated(p1.get(i), p2.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares two annotated types and returns true if they are the same
     */
    public static boolean compareAnnotatedTypes(AnnotatedType<?> t1, AnnotatedType<?> t2) {
        if (!t1.getJavaClass().equals(t2.getJavaClass())) {
            return false;
        }
        if (!compareAnnotated(t1, t2)) {
            return false;
        }

        if (t1.getFields().size() != t2.getFields().size()) {
            return false;
        }
        Map<Field, AnnotatedField<?>> fields = new HashMap<Field, AnnotatedField<?>>();
        for (AnnotatedField<?> f : t2.getFields()) {
            fields.put(f.getJavaMember(), f);
        }
        for (AnnotatedField<?> f : t1.getFields()) {
            if (fields.containsKey(f.getJavaMember())) {
                if (!compareAnnotatedField(f, fields.get(f.getJavaMember()))) {
                    return false;
                }
            } else {
                return false;
            }
        }

        if (t1.getMethods().size() != t2.getMethods().size()) {
            return false;
        }
        Map<Method, AnnotatedMethod<?>> methods = new HashMap<Method, AnnotatedMethod<?>>();
        for (AnnotatedMethod<?> f : t2.getMethods()) {
            methods.put(f.getJavaMember(), f);
        }
        for (AnnotatedMethod<?> f : t1.getMethods()) {
            if (methods.containsKey(f.getJavaMember())) {
                if (!compareAnnotatedCallable(f, methods.get(f.getJavaMember()))) {
                    return false;
                }
            } else {
                return false;
            }
        }
        if (t1.getConstructors().size() != t2.getConstructors().size()) {
            return false;
        }
        Map<Constructor<?>, AnnotatedConstructor<?>> constructors = new HashMap<Constructor<?>, AnnotatedConstructor<?>>();
        for (AnnotatedConstructor<?> f : t2.getConstructors()) {
            constructors.put(f.getJavaMember(), f);
        }
        for (AnnotatedConstructor<?> f : t1.getConstructors()) {
            if (constructors.containsKey(f.getJavaMember())) {
                if (!compareAnnotatedCallable(f, constructors.get(f.getJavaMember()))) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;

    }
}
