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
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.inject.spi.Annotated;

public class AnnotatedWrapper implements Annotated {

    private Annotated delegate;
    private Set<Annotation> annotations;

    public AnnotatedWrapper(Annotated delegate, boolean keepOriginalAnnotations, Annotation... annotations) {
        this.delegate = delegate;
        this.annotations = new HashSet<Annotation>(Arrays.asList(annotations));
        if (keepOriginalAnnotations) {
            this.annotations.addAll(delegate.getAnnotations());
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(Class<T> arg0) {
        for (Annotation annotation : annotations) {
            if (arg0.isAssignableFrom(annotation.annotationType())) {
                return (T) annotation;
            }
        }
        return null;
    }

    public Set<Annotation> getAnnotations() {
        return Collections.unmodifiableSet(annotations);
    }

    public Type getBaseType() {
        return delegate.getBaseType();
    }

    public Set<Type> getTypeClosure() {
        return delegate.getTypeClosure();
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> arg0) {
        for (Annotation annotation : annotations) {
            if (arg0.isAssignableFrom(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T extends Annotation> Set<T> getAnnotations(Class<T> annotationType) {
        return delegate.getAnnotations(annotationType);
    }
}
