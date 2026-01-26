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

package org.jboss.cdi.tck.test.util;

import static org.jboss.cdi.tck.util.Assert.assertAnnotationsMatch;
import static org.jboss.cdi.tck.util.Assert.assertTypesMatch;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.literal.InjectLiteral;
import jakarta.enterprise.util.TypeLiteral;
import jakarta.inject.Inject;

import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 *
 */
public class AssertTest {

    @Test
    public void testAnnotationSetMatches() {
        Set<Annotation> annotations = new HashSet<>();
        annotations.add(Any.Literal.INSTANCE);
        annotations.add(InjectLiteral.INSTANCE);
        assertAnnotationsMatch(annotations, Any.class, Inject.class);
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testAnnotationSetDoesNotMatchA() {
        Set<Annotation> annotations = new HashSet<>();
        annotations.add(Any.Literal.INSTANCE);
        annotations.add(InjectLiteral.INSTANCE);
        assertAnnotationsMatch(annotations, Any.class);
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testAnnotationSetDoesNotMatchB() {
        Set<Annotation> annotations = new HashSet<>();
        annotations.add(InjectLiteral.INSTANCE);
        assertAnnotationsMatch(annotations, Any.class, Inject.class);
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testAnnotationSetDoesNotMatchC() {
        Set<Annotation> annotations = new HashSet<>();
        annotations.add(Any.Literal.INSTANCE);
        annotations.add(InjectLiteral.INSTANCE);
        assertAnnotationsMatch(annotations, Any.class, Default.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAnnotationSetIsEmptyAndRequiredAnnotationsEmpty() {
        assertAnnotationsMatch(new HashSet<>(), new Class[0]);
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testAnnotationSetIsEmpty() {
        assertAnnotationsMatch(new HashSet<>(), Any.class);
    }

    @SuppressWarnings("unchecked")
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAnnotationSetIsNull() {
        assertAnnotationsMatch(null, new Class[0]);
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testTypeSetDoesNotMatch() {
        assertTypesMatch(Arrays.asList(String.class, new TypeLiteral<List<Integer>>() {
        }.getType()), String.class);
    }

    @Test
    public void testTypeSetMatches() {
        assertTypesMatch(
                Arrays.asList(Integer.class, String.class, new TypeLiteral<List<Boolean>>() {
                }.getType()), String.class, Integer.class, new TypeLiteral<List<Boolean>>() {
                }.getType());
    }
}
