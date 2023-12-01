package org.jboss.cdi.tck.tests.build.compatible.extensions.customPseudoScope;

import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.inject.Scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that a bean belongs to the <em>prototype</em> pseudo-scope.
 * <p>
 * When a bean is declared to have the {@code @Prototype} scope:
 * <ul>
 * <li>Each injection point or dynamic lookup receives a new instance; instances are never shared.</li>
 * <li>Lifecycle of instances is not managed by the CDI container.</li>
 * </ul>
 * <p>
 * Every invocation of the {@link Context#get(Contextual, CreationalContext)} operation on the
 * context object for the {@code @Prototype} scope returns a new instance of given bean.
 * <p>
 * Every invocation of the {@link Context#get(Contextual)} operation on the context object for the
 * {@code @Prototype} scope returns a {@code null} value.
 * <p>
 * The {@code @Prototype} scope is always active.
 */
@Scope
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Prototype {
}
