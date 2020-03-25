package org.jboss.cdi.tck.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

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
