/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Set;

import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;

/**
 * Delegating {@link InjectionPoint}.
 *
 * @author Jozef Hartinger
 */
public abstract class ForwardingInjectionPoint implements InjectionPoint {

    protected abstract InjectionPoint delegate();

    public Annotated getAnnotated() {
        return delegate().getAnnotated();
    }

    public Type getType() {
        return delegate().getType();
    }

    public Set<Annotation> getQualifiers() {
        return delegate().getQualifiers();
    }

    public Bean<?> getBean() {
        return delegate().getBean();
    }

    public Member getMember() {
        return delegate().getMember();
    }

    public boolean isDelegate() {
        return delegate().isDelegate();
    }

    public boolean isTransient() {
        return delegate().isTransient();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ForwardingInjectionPoint) {
            return delegate().equals(((ForwardingInjectionPoint) obj).delegate());
        }
        return delegate().equals(obj);
    }

    @Override
    public int hashCode() {
        return delegate().hashCode();
    }

    @Override
    public String toString() {
        return "ForwardingInjectionPoint with type=" + getType()
                + " with qualifiers=" + getQualifiers()
                + " with delegate=" + isDelegate()
                + " with transient=" + isTransient() + ".";
    }
}
