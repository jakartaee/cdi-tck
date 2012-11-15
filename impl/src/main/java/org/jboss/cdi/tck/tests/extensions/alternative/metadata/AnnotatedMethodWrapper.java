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
package org.jboss.cdi.tck.tests.extensions.alternative.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;

public class AnnotatedMethodWrapper<X> extends AnnotatedWrapper implements AnnotatedMethod<X> {

    private AnnotatedMethod<X> delegate;

    private List<AnnotatedParameter<X>> parameters;

    public AnnotatedMethodWrapper(AnnotatedMethod<X> delegate, boolean keepOriginalAnnotations, Annotation... annotations) {
        super(delegate, keepOriginalAnnotations, annotations);
        this.delegate = delegate;
        this.parameters = delegate.getParameters();
    }

    public Method getJavaMember() {
        return delegate.getJavaMember();
    }

    public List<AnnotatedParameter<X>> getParameters() {
        return parameters;
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

    public AnnotatedType<X> getDeclaringType() {
        return delegate.getDeclaringType();
    }

    public boolean isStatic() {
        return delegate.isStatic();
    }

    @Override
    public Set<Annotation> getAnnotations() {
        return super.getAnnotations();
    }

    public AnnotatedMethodWrapper<X> replaceParameters(AnnotatedParameter<X>... parameters) {
        this.parameters = Arrays.asList(parameters);
        return this;
    }

}
