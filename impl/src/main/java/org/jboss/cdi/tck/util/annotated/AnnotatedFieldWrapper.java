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
import java.lang.reflect.Field;
import java.util.Set;

import jakarta.enterprise.inject.spi.AnnotatedField;

public class AnnotatedFieldWrapper<X> extends AnnotatedMemberWrapper<X> implements AnnotatedField<X> {

    private AnnotatedField<X> delegate;

    public AnnotatedFieldWrapper(AnnotatedField<X> delegate, AnnotatedTypeWrapper<X> declaringType,
            boolean keepOriginalAnnotations, Annotation... annotations) {
        super(delegate, declaringType, keepOriginalAnnotations, annotations);
        this.delegate = delegate;
    }

    public Field getJavaMember() {
        return delegate.getJavaMember();
    }

    public AnnotatedTypeWrapper<X> getDeclaringType() {
        return super.getDeclaringType();
    }

    public boolean isStatic() {
        return delegate.isStatic();
    }

    @Override
    public <T extends Annotation> Set<T> getAnnotations(Class<T> annotationType) {
        return delegate.getAnnotations(annotationType);
    }
}
