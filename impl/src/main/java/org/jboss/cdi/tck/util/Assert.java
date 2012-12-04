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
     * 
     * @param annotations
     * @param requiredAnnotationTypes
     * @throws AssertionError if the annotations set and required annotations do not match
     */
    public static void assertAnnotationSetMatches(Set<? extends Annotation> annotations,
            Class<? extends Annotation>... requiredAnnotationTypes) {

        if (annotations.size() != requiredAnnotationTypes.length) {
            fail("Set " + annotations.toString() + " does not match array " + Arrays.toString(requiredAnnotationTypes));
        }

        if (annotations.isEmpty() && requiredAnnotationTypes.length == 0) {
            return;
        }

        List<Class<? extends Annotation>> requiredAnnotationTypesList = Arrays.asList(requiredAnnotationTypes);

        for (Annotation annotation : annotations) {
            if (!requiredAnnotationTypesList.contains(annotation.annotationType())) {
                fail("Set " + annotations.toString() + " does not match array " + Arrays.toString(requiredAnnotationTypes));
            }
        }
    }

    /**
     * 
     * @param types
     * @param requiredTypes
     */
    public static void assertTypeSetMatches(Set<? extends Type> types, Type... requiredTypes) {

        List<Type> requiredTypeList = Arrays.asList(requiredTypes);

        if (requiredTypes.length != types.size() || !types.containsAll(requiredTypeList)) {
            fail("Set " + types.toString() + " does not match array " + requiredTypeList.toString());
        }
    }
}
