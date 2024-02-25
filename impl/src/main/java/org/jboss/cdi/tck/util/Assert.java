/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.AnnotatedMember;
import jakarta.enterprise.inject.spi.AnnotatedParameter;
import jakarta.enterprise.inject.spi.AnnotatedType;

/**
 * @author Martin Kouba
 *
 */
public class Assert {

    private Assert() {
    }

    /**
     *
     * @param annotations
     * @param requiredAnnotationTypes
     * @throws AssertionError if the annotations set and required annotations do not match
     */
    public static void assertAnnotationSetMatches(Set<? extends Annotation> annotations,
            Class<? extends Annotation>... requiredAnnotationTypes) {

        if (annotations == null) {
            throw new IllegalArgumentException();
        }

        if (annotations.size() != requiredAnnotationTypes.length) {
            fail(String.format("Set %s (%s) does not match array %s (%s)", annotations, annotations.size(),
                    Arrays.toString(requiredAnnotationTypes), requiredAnnotationTypes.length));
        }

        if (annotations.isEmpty() && requiredAnnotationTypes.length == 0) {
            return;
        }

        List<Class<? extends Annotation>> requiredAnnotationTypesList = Arrays.asList(requiredAnnotationTypes);

        for (Annotation annotation : annotations) {
            if (!requiredAnnotationTypesList.contains(annotation.annotationType())) {
                fail(String.format("Set %s (%s) does not match array %s (%s)", annotations, annotations.size(),
                        requiredAnnotationTypesList, requiredAnnotationTypesList.size()));
            }
        }
    }

    /**
     *
     * @param types
     * @param requiredTypes
     */
    public static void assertTypeSetMatches(Set<? extends Type> types, Type... requiredTypes) {

        if (types == null) {
            throw new IllegalArgumentException();
        }

        List<Type> requiredTypeList = Arrays.asList(requiredTypes);

        if (requiredTypes.length != types.size() || !types.containsAll(requiredTypeList)) {
            fail(String.format("Set %s (%s) does not match array %s (%s)", types, types.size(), requiredTypeList,
                    requiredTypeList.size()));
        }
    }

    /**
     *
     * @param types
     * @param requiredTypes
     */
    public static void assertTypeListMatches(List<? extends Type> types, Type... requiredTypes) {

        if (types == null) {
            throw new IllegalArgumentException();
        }

        List<Type> requiredTypeList = Arrays.asList(requiredTypes);

        if (requiredTypes.length != types.size() || !types.containsAll(requiredTypeList)) {
            fail(String.format("List %s (%s) does not match array %s (%s)", types, types.size(), requiredTypeList,
                    requiredTypeList.size()));
        }
    }

    /**
     * Helper method to compare 2 Annotated. They don't necessarily implement equals()/hashcode() so we need to
     * compare the underlying java.lang.reflect objects.
     *
     * @param expected The expected Annotated instance
     * @param actual The actual Annotated instance to compare
     */
    public static void assertAnnotated(final Annotated expected, final Annotated actual) {
        assertEquals(unwrap(expected), unwrap(actual));
    }

    private static Object unwrap(final Annotated annotated) {
        if (annotated instanceof AnnotatedMember) {
            return ((AnnotatedMember) annotated).getJavaMember();
        } else if (annotated instanceof AnnotatedParameter) {
            return ((AnnotatedParameter) annotated).getJavaParameter();
        } else if (annotated instanceof AnnotatedType) {
            return ((AnnotatedType) annotated).getJavaClass();
        } else {
            throw new UnsupportedOperationException("Unknown Annotated instance: " + annotated);
        }
    }

}
