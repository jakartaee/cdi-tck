/*
 * Copyright 2014, Red Hat, Inc., and individual contributors
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

import jakarta.enterprise.inject.spi.AnnotatedMember;
import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Set;

public class AnnotatedMemberWrapper<X> extends AnnotatedWrapper implements AnnotatedMember<X> {

    private AnnotatedTypeWrapper<X> declaringType;
    private AnnotatedMember<X> delegate;

    public AnnotatedMemberWrapper(AnnotatedMember delegate, AnnotatedTypeWrapper<X> declaringType, boolean keepOriginalAnnotations, Annotation... annotations) {
        super(delegate, keepOriginalAnnotations, annotations);
        this.delegate = delegate;
        this.declaringType = declaringType;
    }

    @Override
    public Member getJavaMember() {
        return delegate.getJavaMember();
    }

    @Override
    public boolean isStatic() {
        return delegate.isStatic();
    }

    public AnnotatedTypeWrapper<X> getDeclaringType() {
        return declaringType;
    }

    @Override
    public Type getBaseType() {
        return delegate.getBaseType();
    }

    @Override
    public Set<Type> getTypeClosure() {
        return delegate.getTypeClosure();
    }
}
