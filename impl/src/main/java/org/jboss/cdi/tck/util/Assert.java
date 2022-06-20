/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.util;

import static org.testng.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Martin Kouba
 *
 */
public class Assert {

    private Assert() {
    }

    /**
     * Validate that types match.
     *
     * @param type The provided type
     * @param requiredType The expected type
     */
    public static void assertTypeEquals(Type type, Type requiredType) {
        if (!isTypesEqual(type, requiredType)) {
            fail(String.format("Type '%s' does not match type '%s'", type, requiredType));
        }
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
            fail(String.format("Set %s (%s) does not match array %s (%s)", annotations, annotations.size(), Arrays.toString(requiredAnnotationTypes), requiredAnnotationTypes.length));
        }

        if (annotations.isEmpty() && requiredAnnotationTypes.length == 0) {
            return;
        }

        List<Class<? extends Annotation>> requiredAnnotationTypesList = Arrays.asList(requiredAnnotationTypes);

        for (Annotation annotation : annotations) {
            if (!isInstanceOf(annotation, requiredAnnotationTypesList)) {
                fail(String.format("Set %s (%s) does not match array %s (%s)", annotations, annotations.size(), requiredAnnotationTypesList, requiredAnnotationTypesList.size()));
            }
        }
    }

    /**
     *
     * @param types
     * @param requiredTypes
     */
    public static void assertTypeSetMatches(Set<? extends Type> types, Type... requiredTypes) {

        if(types == null) {
            throw new IllegalArgumentException();
        }

        List<Type> requiredTypeList = Arrays.asList(requiredTypes);

        if (requiredTypes.length != types.size() || !containsAllTypes(types, requiredTypeList)) {
            fail(String.format("Set %s (%s) does not match array %s (%s)", types, types.size(), requiredTypeList, requiredTypeList.size()));
        }
    }

    /**
     *
     * @param types
     * @param requiredTypes
     */
    public static void assertTypeListMatches(List<? extends Type> types, Type... requiredTypes) {

        if(types == null) {
            throw new IllegalArgumentException();
        }

        List<Type> requiredTypeList = Arrays.asList(requiredTypes);

        if (requiredTypes.length != types.size() || !containsAllTypes(types, requiredTypeList)) {
            fail(String.format("List %s (%s) does not match array %s (%s)", types, types.size(), requiredTypeList, requiredTypeList.size()));
        }
    }

    private static boolean containsAllTypes(Collection<? extends Type> existingTypes, Collection<? extends Type> requiredTypes) {
        if (requiredTypes.isEmpty()) {
            return false;
        }
        for (Type requiredType : requiredTypes) {
            if (!containsType(existingTypes, requiredType)) {
                return false;
            }
        }
        return true;
    }

    private static boolean containsType(Collection<? extends Type> existingTypes, Type requiredType) {
        if (existingTypes.isEmpty()) {
            return false;
        }
        for (Type existingType : existingTypes) {
            if (isTypesEqual(requiredType, existingType)) {
                return true;
            }
        }
        return false;
    }

    private static Boolean isTypesEqual(Type type, Type requiredType) {
        return type.getTypeName().equals(requiredType.getTypeName());
    }

    private static boolean isInstanceOf(Annotation annotation, Collection<? extends Class<? extends Annotation>> requiredTypes) {
        if (requiredTypes.isEmpty()) {
            return false;
        }
        for (Class<? extends Annotation> requiredType : requiredTypes) {
            if (requiredType.isInstance(annotation)) {
                return true;
            }
        }
        return false;
    }

}
