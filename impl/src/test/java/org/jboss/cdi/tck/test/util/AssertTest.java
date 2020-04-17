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

package org.jboss.cdi.tck.test.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.literal.InjectLiteral;
import jakarta.enterprise.util.TypeLiteral;
import jakarta.inject.Inject;

import org.jboss.cdi.tck.util.Assert;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 *
 */
public class AssertTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testAnnotationSetMatches() {
        Set<Annotation> annotations = new HashSet<Annotation>();
        annotations.add(Any.Literal.INSTANCE);
        annotations.add(InjectLiteral.INSTANCE);
        Assert.assertAnnotationSetMatches(annotations, Any.class, Inject.class);
    }

    @SuppressWarnings("unchecked")
    @Test(expectedExceptions = AssertionError.class)
    public void testAnnotationSetDoesNotMatchA() {
        Set<Annotation> annotations = new HashSet<Annotation>();
        annotations.add(Any.Literal.INSTANCE);
        annotations.add(InjectLiteral.INSTANCE);
        Assert.assertAnnotationSetMatches(annotations, Any.class);
    }

    @SuppressWarnings("unchecked")
    @Test(expectedExceptions = AssertionError.class)
    public void testAnnotationSetDoesNotMatchB() {
        Set<Annotation> annotations = new HashSet<Annotation>();
        annotations.add(InjectLiteral.INSTANCE);
        Assert.assertAnnotationSetMatches(annotations, Any.class, Inject.class);
    }

    @SuppressWarnings("unchecked")
    @Test(expectedExceptions = AssertionError.class)
    public void testAnnotationSetDoesNotMatchC() {
        Set<Annotation> annotations = new HashSet<Annotation>();
        annotations.add(Any.Literal.INSTANCE);
        annotations.add(InjectLiteral.INSTANCE);
        Assert.assertAnnotationSetMatches(annotations, Any.class, Default.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAnnotationSetIsEmptyAndRequiredAnnotationsEmpty() {
        Assert.assertAnnotationSetMatches(new HashSet<Annotation>());
    }

    @SuppressWarnings("unchecked")
    @Test(expectedExceptions = AssertionError.class)
    public void testAnnotationSetIsEmpty() {
        Assert.assertAnnotationSetMatches(new HashSet<Annotation>(), Any.class);
    }

    @SuppressWarnings("unchecked")
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAnnotationSetIsNull() {
        Assert.assertAnnotationSetMatches(null);
    }

    @SuppressWarnings("serial")
    @Test(expectedExceptions = AssertionError.class)
    public void testTypeSetDoeNotMatch() {
        Assert.assertTypeSetMatches(new HashSet<Type>(Arrays.asList(String.class, new TypeLiteral<List<Integer>>() {
        }.getType())), String.class);
    }

    @SuppressWarnings("serial")
    @Test
    public void testTypeSetMatches() {
        Assert.assertTypeSetMatches(
                new HashSet<Type>(Arrays.asList(Integer.class, String.class, new TypeLiteral<List<Boolean>>() {
                }.getType())), String.class, Integer.class, new TypeLiteral<List<Boolean>>() {
                }.getType());
    }
}
