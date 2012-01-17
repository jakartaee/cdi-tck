/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.annotated;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;

public class TestAnnotatedType<X> extends TestAnnotated implements AnnotatedType<X> {
    private AnnotatedType<X> delegate;
    private static boolean getConstructorsUsed = false;
    private static boolean getFieldsUsed = false;
    private static boolean getMethodsUsed = false;

    public TestAnnotatedType(AnnotatedType<X> delegate, Annotation... annotations) {
        super(delegate, annotations);
        this.delegate = delegate;
    }

    public Set<AnnotatedConstructor<X>> getConstructors() {
        getConstructorsUsed = true;
        return delegate.getConstructors();
    }

    public Set<AnnotatedField<? super X>> getFields() {
        getFieldsUsed = true;
        return delegate.getFields();
    }

    public Class<X> getJavaClass() {
        return delegate.getJavaClass();
    }

    public Set<AnnotatedMethod<? super X>> getMethods() {
        getMethodsUsed = true;
        return delegate.getMethods();
    }

    public static boolean isGetConstructorsUsed() {
        return getConstructorsUsed;
    }

    public static boolean isGetFieldsUsed() {
        return getFieldsUsed;
    }

    public static boolean isGetMethodsUsed() {
        return getMethodsUsed;
    }
}
