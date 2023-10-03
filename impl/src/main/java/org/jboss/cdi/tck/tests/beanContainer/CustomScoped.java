package org.jboss.cdi.tck.tests.beanContainer;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.enterprise.context.NormalScope;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@NormalScope
@Inherited
@Target({ TYPE, METHOD, FIELD })
@Retention(RUNTIME)
public @interface CustomScoped {
}
