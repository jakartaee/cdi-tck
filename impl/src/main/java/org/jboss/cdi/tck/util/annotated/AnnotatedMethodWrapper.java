/*
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
package org.jboss.cdi.tck.util.annotated;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedParameter;

public class AnnotatedMethodWrapper<X> extends AnnotatedCallableWraper<X> implements AnnotatedMethod<X> {

    private AnnotatedMethod<X> delegate;

    public AnnotatedMethodWrapper(AnnotatedMethod<X> delegate,AnnotatedTypeWrapper<X> declaringType, boolean keepOriginalAnnotations, Annotation... annotations ) {
        super(delegate, declaringType,keepOriginalAnnotations, annotations);
        this.delegate = delegate;
    }

    public Method getJavaMember() {
        return delegate.getJavaMember();
    }


    public AnnotatedParameter<X> getParameter(int position) {
      return super.getParameters().get(position);
    }

    public AnnotatedTypeWrapper getDeclaringType() {
        return super.getDeclaringType();
    }

    public boolean isStatic() {
        return delegate.isStatic();
    }

    @Override
    public Set<Annotation> getAnnotations() {
        return super.getAnnotations();
    }

    @Override
    public <T extends Annotation> Set<T> getAnnotations(Class<T> annotationType) {
        return delegate.getAnnotations(annotationType);
    }


}
