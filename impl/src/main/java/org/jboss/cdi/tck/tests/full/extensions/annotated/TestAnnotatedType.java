/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.annotated;

import java.util.Set;

import jakarta.enterprise.inject.spi.AnnotatedConstructor;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedType;

import org.jboss.cdi.tck.util.ForwardingAnnotatedType;

/** Makes use of util class ForwardingAnnotatedType&lt;X&gt;
 * 
 */
public class TestAnnotatedType<X> extends ForwardingAnnotatedType<X> {
    private AnnotatedType<X> delegate;
    private static boolean getConstructorsUsed = false;
    private static boolean getFieldsUsed = false;
    private static boolean getMethodsUsed = false;

    public TestAnnotatedType(AnnotatedType<X> delegate) {
        this.delegate = delegate;
    }

    @Override
    public AnnotatedType<X> delegate() {
        return delegate;
    }

    @Override
    public Set<AnnotatedConstructor<X>> getConstructors() {
        getConstructorsUsed = true;
        return super.getConstructors();
    }

    @Override
    public Set<AnnotatedField<? super X>> getFields() {
        getFieldsUsed = true;
        return super.getFields();
    }

    @Override
    public Set<AnnotatedMethod<? super X>> getMethods() {
        getMethodsUsed = true;
        return super.getMethods();
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
