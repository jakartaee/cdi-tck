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
package org.jboss.cdi.tck.util.annotated;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jakarta.enterprise.inject.spi.AnnotatedConstructor;
import jakarta.enterprise.inject.spi.AnnotatedParameter;

public class AnnotatedConstructorWrapper<X> extends AnnotatedCallableWraper<X> implements AnnotatedConstructor<X> {

    private AnnotatedConstructor<X> delegate;
    private List<AnnotatedParameter<X>> parameters;

    public AnnotatedConstructorWrapper(AnnotatedConstructor<X> delegate, AnnotatedTypeWrapper<X> declaringType,
            boolean keepOriginalAnnotations,
            Annotation... annotations) {
        super(delegate, declaringType, keepOriginalAnnotations, annotations);
        this.delegate = delegate;
        this.parameters = delegate.getParameters();
    }

    public Constructor<X> getJavaMember() {
        return delegate.getJavaMember();
    }

    public List<AnnotatedParameter<X>> getParameters() {
        return this.parameters;
    }

    public AnnotatedTypeWrapper<X> getDeclaringType() {
        return super.getDeclaringType();
    }

    public boolean isStatic() {
        return delegate.isStatic();
    }

    public AnnotatedConstructorWrapper<X> replaceParameters(AnnotatedParameter<X>... parameters) {
        this.parameters = Arrays.asList(parameters);
        return this;
    }

    public AnnotatedParameter<X> getParameter(int position) {
        for (Iterator<AnnotatedParameter<X>> iterator = this.parameters.iterator(); iterator.hasNext();) {
            AnnotatedParameter<X> parameter = iterator.next();
            if (parameter.getPosition() == position) {
                return parameter;
            }
        }
        return null;
    }
}
