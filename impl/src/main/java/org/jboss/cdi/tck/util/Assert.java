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

import static org.testng.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Martin Kouba
 *
 */
public class Assert {

    private Assert() {
    }

    /**
     * Asserts that given collection of {@code annotations} contains annotations of all given
     * {@code requiredAnnotationTypes} and no other.
     *
     * @param annotations the collection of annotations to check
     * @param requiredAnnotationTypes the annotation types to match
     */
    @SafeVarargs
    public static void assertAnnotationsMatch(Collection<? extends Annotation> annotations,
            Class<? extends Annotation>... requiredAnnotationTypes) {

        if (annotations == null) {
            throw new IllegalArgumentException();
        }

        if (annotations.size() != requiredAnnotationTypes.length) {
            fail(String.format("Collection %s (%s) does not match array %s (%s)", annotations, annotations.size(),
                    Arrays.toString(requiredAnnotationTypes), requiredAnnotationTypes.length));
        }

        if (annotations.isEmpty()) {
            return;
        }

        List<Class<? extends Annotation>> requiredAnnotationTypesList = Arrays.asList(requiredAnnotationTypes);

        for (Annotation annotation : annotations) {
            if (!requiredAnnotationTypesList.contains(annotation.annotationType())) {
                fail(String.format("Collection %s (%s) does not match array %s (%s)", annotations, annotations.size(),
                        requiredAnnotationTypesList, requiredAnnotationTypesList.size()));
            }
        }
    }

    /**
     * Asserts that given collection of {@code annotations} contains all given {@code requiredAnnotations} and no other.
     *
     * @param annotations the collection of annotations to check
     * @param requiredAnnotations the annotations to match
     */
    public static void assertAnnotationsMatch(Collection<? extends Annotation> annotations,
            Annotation... requiredAnnotations) {
        if (annotations == null) {
            throw new IllegalArgumentException();
        }

        List<Annotation> requiredAnnotationList = Arrays.asList(requiredAnnotations);

        if (requiredAnnotations.length != annotations.size() || !annotations.containsAll(requiredAnnotationList)) {
            fail(String.format("Collection %s (%s) does not match array %s (%s)", annotations, annotations.size(),
                    requiredAnnotationList, requiredAnnotationList.size()));
        }
    }

    /**
     * Asserts that given collection of {@code types} contains all given {@code requiredTypes} and no other.
     *
     * @param types the collection of types to check
     * @param requiredTypes the types to match
     */
    public static void assertTypesMatch(Collection<? extends Type> types, Type... requiredTypes) {
        if (types == null) {
            throw new IllegalArgumentException();
        }

        List<Type> requiredTypeList = Arrays.asList(requiredTypes);

        if (types.size() != requiredTypes.length || !types.containsAll(requiredTypeList)) {
            fail(String.format("Collection %s (%s) does not match array %s (%s)", types, types.size(), requiredTypeList,
                    requiredTypeList.size()));
        }
    }
}
