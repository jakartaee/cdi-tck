package org.jboss.jsr299.tck.tests.interceptors.definition.broken.invalidBindingAnnotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.inject.Stereotype;

@Target( { TYPE, METHOD, FIELD })
@Retention(RUNTIME)
@Documented
@FooBinding("abc")
public @Stereotype
@interface FooStereotype
{

}
