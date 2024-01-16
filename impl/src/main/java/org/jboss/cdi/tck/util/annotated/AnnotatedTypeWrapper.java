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

import jakarta.enterprise.inject.spi.AnnotatedConstructor;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedType;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class AnnotatedTypeWrapper<X> extends AnnotatedWrapper implements AnnotatedType<X> {
    private AnnotatedType<X> delegate;
    private Set<AnnotatedConstructor<X>> constructors;
    private Set<AnnotatedField<? super X>> fields;
    private Set<AnnotatedMethod<? super X>> methods;

    public AnnotatedTypeWrapper(AnnotatedType<X> delegate, boolean keepOriginalAnnotations, Annotation... annotations) {
        super(delegate, keepOriginalAnnotations, annotations);
        this.delegate = delegate;
        this.constructors = new HashSet<AnnotatedConstructor<X>>();
        for (AnnotatedConstructor<X> constructor : delegate.getConstructors()) {
            constructors.add(new AnnotatedConstructorWrapper<X>(constructor, this, true, constructor.getAnnotations().toArray(
                    new Annotation[] { })));
        }

        this.fields = new HashSet<AnnotatedField<? super X>>();
        for (AnnotatedField<? super X> field : delegate.getFields()) {
            fields.add(new AnnotatedFieldWrapper(field, this, true, field.getAnnotations().toArray(new Annotation[] { })));
        }

        this.methods = new HashSet<AnnotatedMethod<? super X>>();
        for (AnnotatedMethod<? super X> method : delegate.getMethods()) {
            methods.add(new AnnotatedMethodWrapper(method, this, true, method.getAnnotations().toArray(new Annotation[] { })));
        }

    }

    public Set<AnnotatedConstructor<X>> getConstructors() {
        return constructors;
    }

    public Set<AnnotatedField<? super X>> getFields() {
        return fields;
    }

    public Class<X> getJavaClass() {
        return delegate.getJavaClass();
    }

    public Set<AnnotatedMethod<? super X>> getMethods() {
        return methods;
    }
}